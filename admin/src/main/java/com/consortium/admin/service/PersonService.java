package com.consortium.admin.service;

import com.consortium.admin.dto.PersonRequest;
import com.consortium.admin.dto.PersonResponse;
import com.consortium.admin.entity.Person;
import com.consortium.admin.exception.DuplicateEmailException;
import com.consortium.admin.exception.PersonNotFoundException;
import com.consortium.admin.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 200;

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<PersonResponse> findAll() {
        log.debug("Fetching all persons");
        return personRepository.findAll()
                .stream()
                .map(PersonResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PersonResponse findById(Long id) {
        log.debug("Fetching person with id={}", id);
        return personRepository.findById(id)
                .map(PersonResponse::from)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    @Transactional
    public PersonResponse create(PersonRequest request) {
        log.debug("Creating person with email={}", request.getEmail());
        if (personRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }
        Person person = new Person(request.getName(), request.getEmail(), request.getAvatar());
        Person saved = executeWithRetry(() -> personRepository.save(person));
        log.info("Created person id={}", saved.getId());
        return PersonResponse.from(saved);
    }

    @Transactional
    public PersonResponse update(Long id, PersonRequest request) {
        log.debug("Updating person id={}", id);
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        if (!person.getEmail().equals(request.getEmail())
                && personRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        person.setName(request.getName());
        person.setEmail(request.getEmail());
        person.setAvatar(request.getAvatar());
        Person saved = executeWithRetry(() -> personRepository.save(person));
        log.info("Updated person id={}", saved.getId());
        return PersonResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Deleting person id={}", id);
        if (!personRepository.existsById(id)) {
            throw new PersonNotFoundException(id);
        }
        executeWithRetry(() -> {
            personRepository.deleteById(id);
            return null;
        });
        log.info("Deleted person id={}", id);
    }

    /**
     * Retries the given write operation on SQLite "database is locked" errors.
     * SQLite allows only one writer at a time; under concurrent load writes may fail transiently.
     */
    private <T> T executeWithRetry(WriteOperation<T> operation) {
        int attempt = 0;
        while (true) {
            try {
                return operation.execute();
            } catch (DataIntegrityViolationException e) {
                throw e;
            } catch (Exception e) {
                if (isDatabaseLocked(e) && attempt < MAX_RETRIES) {
                    attempt++;
                    log.warn("SQLite database locked, retrying ({}/{})", attempt, MAX_RETRIES);
                    try {
                        Thread.sleep(RETRY_DELAY_MS * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted during retry", ie);
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    private boolean isDatabaseLocked(Throwable e) {
        Throwable cause = e;
        while (cause != null) {
            String msg = cause.getMessage();
            if (msg != null && msg.toLowerCase().contains("database is locked")) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    @FunctionalInterface
    private interface WriteOperation<T> {
        T execute();
    }
}
