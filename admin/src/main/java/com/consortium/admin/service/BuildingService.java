package com.consortium.admin.service;

import com.consortium.admin.dto.BuildingRequest;
import com.consortium.admin.dto.BuildingResponse;
import com.consortium.admin.entity.Building;
import com.consortium.admin.exception.BuildingNotFoundException;
import com.consortium.admin.repository.BuildingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuildingService {

    private static final Logger log = LoggerFactory.getLogger(BuildingService.class);

    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @Transactional(readOnly = true)
    public List<BuildingResponse> findAll() {
        log.debug("Fetching all buildings");
        return buildingRepository.findAllByDeletedFalse()
                .stream()
                .map(BuildingResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BuildingResponse findById(Long id) {
        log.debug("Fetching building with id={}", id);
        return buildingRepository.findByIdAndDeletedFalse(id)
                .map(BuildingResponse::from)
                .orElseThrow(() -> new BuildingNotFoundException(id));
    }

    @Transactional
    public BuildingResponse create(BuildingRequest request) {
        log.debug("Creating building name={}", request.getName());
        Building building = new Building(
                request.getDirection(),
                request.getLatitude(),
                request.getLongitude(),
                request.getName()
        );
        Building saved = RetryUtil.executeWithRetry(() -> buildingRepository.save(building));
        log.info("Created building id={}", saved.getId());
        return BuildingResponse.from(saved);
    }

    @Transactional
    public BuildingResponse update(Long id, BuildingRequest request) {
        log.debug("Updating building id={}", id);
        Building building = buildingRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BuildingNotFoundException(id));

        building.setDirection(request.getDirection());
        building.setLatitude(request.getLatitude());
        building.setLongitude(request.getLongitude());
        building.setName(request.getName());
        Building saved = RetryUtil.executeWithRetry(() -> buildingRepository.save(building));
        log.info("Updated building id={}", saved.getId());
        return BuildingResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Deleting (logically) building id={}", id);
        Building building = buildingRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BuildingNotFoundException(id));
        building.setDeleted(true);
        RetryUtil.executeWithRetry(() -> buildingRepository.save(building));
        log.info("Logically deleted building id={}", id);
    }
}
