package com.consortium.admin.controller;

import com.consortium.admin.dto.IssueRequest;
import com.consortium.admin.dto.IssueResponse;
import com.consortium.admin.dto.IssueSpentRequest;
import com.consortium.admin.dto.IssueUnitRequest;
import com.consortium.admin.service.IssueService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private static final Logger log = LoggerFactory.getLogger(IssueController.class);

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public ResponseEntity<List<IssueResponse>> findAll() {
        log.debug("GET /api/issues");
        return ResponseEntity.ok(issueService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponse> findById(@PathVariable Long id) {
        log.debug("GET /api/issues/{}", id);
        return ResponseEntity.ok(issueService.findById(id));
    }

    @PostMapping
    public ResponseEntity<IssueResponse> create(@Valid @RequestBody IssueRequest request) {
        log.debug("POST /api/issues");
        IssueResponse created = issueService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IssueResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody IssueRequest request) {
        log.debug("PUT /api/issues/{}", id);
        return ResponseEntity.ok(issueService.update(id, request));
    }

    @PostMapping(value = "/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<IssueResponse> attachFile(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) throws IOException {
        log.debug("POST /api/issues/{}/attachments", id);
        return ResponseEntity.ok(issueService.attachFile(id, file));
    }

    @PostMapping("/{id}/units")
    public ResponseEntity<IssueResponse> addObserverUnit(
            @PathVariable Long id,
            @Valid @RequestBody IssueUnitRequest request) {
        log.debug("POST /api/issues/{}/units", id);
        return ResponseEntity.ok(issueService.addObserverUnit(id, request));
    }

    @PostMapping("/{id}/spents")
    public ResponseEntity<IssueResponse> addSpent(
            @PathVariable Long id,
            @Valid @RequestBody IssueSpentRequest request) {
        log.debug("POST /api/issues/{}/spents", id);
        return ResponseEntity.ok(issueService.addSpent(id, request));
    }
}
