package com.consortium.admin.service;

import com.consortium.admin.dto.BuildingUnitRequest;
import com.consortium.admin.dto.BuildingUnitResponse;
import com.consortium.admin.entity.Building;
import com.consortium.admin.entity.BuildingUnit;
import com.consortium.admin.entity.Person;
import com.consortium.admin.exception.BuildingNotFoundException;
import com.consortium.admin.exception.BuildingUnitNotFoundException;
import com.consortium.admin.exception.PersonNotFoundException;
import com.consortium.admin.repository.BuildingRepository;
import com.consortium.admin.repository.BuildingUnitRepository;
import com.consortium.admin.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuildingUnitService {

    private static final Logger log = LoggerFactory.getLogger(BuildingUnitService.class);

    private final BuildingUnitRepository buildingUnitRepository;
    private final BuildingRepository buildingRepository;
    private final PersonRepository personRepository;

    public BuildingUnitService(BuildingUnitRepository buildingUnitRepository,
                               BuildingRepository buildingRepository,
                               PersonRepository personRepository) {
        this.buildingUnitRepository = buildingUnitRepository;
        this.buildingRepository = buildingRepository;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<BuildingUnitResponse> findAll() {
        log.debug("Fetching all building units");
        return buildingUnitRepository.findAll()
                .stream()
                .map(BuildingUnitResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BuildingUnitResponse findById(Long id) {
        log.debug("Fetching building unit with id={}", id);
        return buildingUnitRepository.findById(id)
                .map(BuildingUnitResponse::from)
                .orElseThrow(() -> new BuildingUnitNotFoundException(id));
    }

    @Transactional
    public BuildingUnitResponse create(BuildingUnitRequest request) {
        log.debug("Creating building unit for buildingId={}", request.getBuildingId());
        Building building = buildingRepository.findByIdAndDeletedFalse(request.getBuildingId())
                .orElseThrow(() -> new BuildingNotFoundException(request.getBuildingId()));
        Person owner = personRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new PersonNotFoundException(request.getOwnerId()));

        int nextNumber = buildingUnitRepository
                .findMaxNumberByBuildingId(building.getId())
                .orElse(0) + 1;

        BuildingUnit unit = new BuildingUnit(building, nextNumber, owner, request.getCoefficient());
        BuildingUnit saved = RetryUtil.executeWithRetry(() -> buildingUnitRepository.save(unit));
        log.info("Created building unit id={} number={}", saved.getId(), saved.getNumber());
        return BuildingUnitResponse.from(saved);
    }

    @Transactional
    public BuildingUnitResponse update(Long id, BuildingUnitRequest request) {
        log.debug("Updating building unit id={}", id);
        BuildingUnit unit = buildingUnitRepository.findById(id)
                .orElseThrow(() -> new BuildingUnitNotFoundException(id));

        Building building = buildingRepository.findByIdAndDeletedFalse(request.getBuildingId())
                .orElseThrow(() -> new BuildingNotFoundException(request.getBuildingId()));
        Person owner = personRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new PersonNotFoundException(request.getOwnerId()));

        unit.setBuilding(building);
        unit.setOwner(owner);
        unit.setCoefficient(request.getCoefficient());
        BuildingUnit saved = RetryUtil.executeWithRetry(() -> buildingUnitRepository.save(unit));
        log.info("Updated building unit id={}", saved.getId());
        return BuildingUnitResponse.from(saved);
    }
}
