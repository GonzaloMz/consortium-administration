package com.consortium.admin.controller;

import com.consortium.admin.dto.BuildingUnitRequest;
import com.consortium.admin.dto.BuildingUnitResponse;
import com.consortium.admin.service.BuildingUnitService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/building-units")
public class BuildingUnitController {

    private static final Logger log = LoggerFactory.getLogger(BuildingUnitController.class);

    private final BuildingUnitService buildingUnitService;

    public BuildingUnitController(BuildingUnitService buildingUnitService) {
        this.buildingUnitService = buildingUnitService;
    }

    @GetMapping
    public ResponseEntity<List<BuildingUnitResponse>> findAll() {
        log.debug("GET /api/building-units");
        return ResponseEntity.ok(buildingUnitService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildingUnitResponse> findById(@PathVariable Long id) {
        log.debug("GET /api/building-units/{}", id);
        return ResponseEntity.ok(buildingUnitService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BuildingUnitResponse> create(@Valid @RequestBody BuildingUnitRequest request) {
        log.debug("POST /api/building-units");
        BuildingUnitResponse created = buildingUnitService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildingUnitResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BuildingUnitRequest request) {
        log.debug("PUT /api/building-units/{}", id);
        return ResponseEntity.ok(buildingUnitService.update(id, request));
    }
}
