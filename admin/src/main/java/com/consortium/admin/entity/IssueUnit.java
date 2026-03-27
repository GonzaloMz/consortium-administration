package com.consortium.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Represents a building unit associated with an issue (observer unit).
 */
@Entity
@Table(name = "issue_units")
public class IssueUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Unit is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    private BuildingUnit unit;

    @NotNull(message = "Issue is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @Column
    private LocalDate updateDate;

    @PrePersist
    protected void onCreate() {
        if (creationDate == null) {
            creationDate = LocalDate.now();
        }
        if (updateDate == null) {
            updateDate = creationDate;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDate.now();
    }

    public IssueUnit() {
    }

    public IssueUnit(BuildingUnit unit, Issue issue) {
        this.unit = unit;
        this.issue = issue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BuildingUnit getUnit() {
        return unit;
    }

    public void setUnit(BuildingUnit unit) {
        this.unit = unit;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}
