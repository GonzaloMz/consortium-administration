package com.consortium.admin.controller;

import com.consortium.admin.dto.BuildingRequest;
import com.consortium.admin.dto.BuildingResponse;
import com.consortium.admin.service.BuildingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    private static final Logger log = LoggerFactory.getLogger(BuildingController.class);

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping
    public ResponseEntity<List<BuildingResponse>> findAll() {
        log.debug("GET /api/buildings");
        return ResponseEntity.ok(buildingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildingResponse> findById(@PathVariable Long id) {
        log.debug("GET /api/buildings/{}", id);
        return ResponseEntity.ok(buildingService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BuildingResponse> create(@Valid @RequestBody BuildingRequest request) {
        log.debug("POST /api/buildings");
        BuildingResponse created = buildingService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildingResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BuildingRequest request) {
        log.debug("PUT /api/buildings/{}", id);
        return ResponseEntity.ok(buildingService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("DELETE /api/buildings/{}", id);
        buildingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
