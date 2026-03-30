package com.consortium.admin.service;

import com.consortium.admin.dto.IssueAttachmentRequest;
import com.consortium.admin.dto.IssueRequest;
import com.consortium.admin.dto.IssueResponse;
import com.consortium.admin.dto.IssueSpentRequest;
import com.consortium.admin.dto.IssueUnitRequest;
import com.consortium.admin.entity.Building;
import com.consortium.admin.entity.BuildingUnit;
import com.consortium.admin.entity.Issue;
import com.consortium.admin.entity.IssueAttachment;
import com.consortium.admin.entity.IssueSpent;
import com.consortium.admin.entity.IssueUnit;
import com.consortium.admin.exception.BuildingNotFoundException;
import com.consortium.admin.exception.BuildingUnitNotFoundException;
import com.consortium.admin.exception.IssueNotFoundException;
import com.consortium.admin.repository.BuildingRepository;
import com.consortium.admin.repository.BuildingUnitRepository;
import com.consortium.admin.repository.IssueAttachmentRepository;
import com.consortium.admin.repository.IssueRepository;
import com.consortium.admin.repository.IssueSpentRepository;
import com.consortium.admin.repository.IssueUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IssueService {

    private static final Logger log = LoggerFactory.getLogger(IssueService.class);

    private final IssueRepository issueRepository;
    private final BuildingRepository buildingRepository;
    private final BuildingUnitRepository buildingUnitRepository;
    private final IssueUnitRepository issueUnitRepository;
    private final IssueAttachmentRepository issueAttachmentRepository;
    private final IssueSpentRepository issueSpentRepository;

    public IssueService(IssueRepository issueRepository,
                        BuildingRepository buildingRepository,
                        BuildingUnitRepository buildingUnitRepository,
                        IssueUnitRepository issueUnitRepository,
                        IssueAttachmentRepository issueAttachmentRepository,
                        IssueSpentRepository issueSpentRepository) {
        this.issueRepository = issueRepository;
        this.buildingRepository = buildingRepository;
        this.buildingUnitRepository = buildingUnitRepository;
        this.issueUnitRepository = issueUnitRepository;
        this.issueAttachmentRepository = issueAttachmentRepository;
        this.issueSpentRepository = issueSpentRepository;
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> findAll() {
        log.debug("Fetching all issues");
        return issueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public IssueResponse findById(Long id) {
        log.debug("Fetching issue with id={}", id);
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException(id));
        return toResponse(issue);
    }

    @Transactional
    public IssueResponse create(IssueRequest request) {
        log.debug("Creating issue for buildingId={}", request.getBuildingId());
        Building building = buildingRepository.findByIdAndDeletedFalse(request.getBuildingId())
                .orElseThrow(() -> new BuildingNotFoundException(request.getBuildingId()));
        Issue issue = new Issue(building, request.getDetail(), request.isCommonExpense());
        Issue saved = RetryUtil.executeWithRetry(() -> issueRepository.save(issue));
        log.info("Created issue id={}", saved.getId());
        return toResponse(saved);
    }

    @Transactional
    public IssueResponse update(Long id, IssueRequest request) {
        log.debug("Updating issue id={}", id);
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException(id));
        Building building = buildingRepository.findByIdAndDeletedFalse(request.getBuildingId())
                .orElseThrow(() -> new BuildingNotFoundException(request.getBuildingId()));

        issue.setBuilding(building);
        issue.setDetail(request.getDetail());
        issue.setCommonExpense(request.isCommonExpense());
        Issue saved = RetryUtil.executeWithRetry(() -> issueRepository.save(issue));
        log.info("Updated issue id={}", saved.getId());
        return toResponse(saved);
    }

    @Transactional
    public IssueResponse attachFile(Long id, IssueAttachmentRequest request) {
        log.debug("Attaching file to issue id={}", id);
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException(id));
        IssueAttachment attachment = new IssueAttachment(request.getAttachment(), issue);
        RetryUtil.executeWithRetry(() -> issueAttachmentRepository.save(attachment));
        log.info("Attached file to issue id={}", id);
        return toResponse(issue);
    }

    @Transactional
    public IssueResponse addObserverUnit(Long id, IssueUnitRequest request) {
        log.debug("Adding observer unit to issue id={}", id);
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException(id));
        BuildingUnit unit = buildingUnitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new BuildingUnitNotFoundException(request.getUnitId()));
        IssueUnit issueUnit = new IssueUnit(unit, issue);
        RetryUtil.executeWithRetry(() -> issueUnitRepository.save(issueUnit));
        log.info("Added observer unit {} to issue id={}", unit.getId(), id);
        return toResponse(issue);
    }

    @Transactional
    public IssueResponse addSpent(Long id, IssueSpentRequest request) {
        log.debug("Adding spent to issue id={}", id);
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException(id));
        IssueSpent spent = new IssueSpent(request.getValue(), request.getProvider(), issue);
        RetryUtil.executeWithRetry(() -> issueSpentRepository.save(spent));
        log.info("Added spent to issue id={}", id);
        return toResponse(issue);
    }

    private IssueResponse toResponse(Issue issue) {
        List<IssueAttachment> attachments = issueAttachmentRepository.findAllByIssueId(issue.getId());
        List<IssueUnit> units = issueUnitRepository.findAllByIssueId(issue.getId());
        List<IssueSpent> spents = issueSpentRepository.findAllByIssueId(issue.getId());
        return IssueResponse.from(issue, attachments, units, spents);
    }
}
