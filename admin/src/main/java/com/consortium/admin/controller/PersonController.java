package com.consortium.admin.controller;

import com.consortium.admin.dto.PersonRequest;
import com.consortium.admin.dto.PersonResponse;
import com.consortium.admin.service.PersonService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> findAll() {
        log.debug("GET /api/persons");
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> findById(@PathVariable Long id) {
        log.debug("GET /api/persons/{}", id);
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@Valid @RequestBody PersonRequest request) {
        log.debug("POST /api/persons");
        PersonResponse created = personService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PersonRequest request) {
        log.debug("PUT /api/persons/{}", id);
        return ResponseEntity.ok(personService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("DELETE /api/persons/{}", id);
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
