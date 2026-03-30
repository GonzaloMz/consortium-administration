package com.consortium.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


/**
 * Represents a building unit associated with an issue (observer unit).
 */
@Entity
@Table(name = "issue_units")
public class IssueUnit extends AbstractIssue {

    @NotNull(message = "Unit is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    private BuildingUnit unit;

    @NotNull(message = "Issue is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    public IssueUnit() {
    }

    public IssueUnit(BuildingUnit unit, Issue issue) {
        this.unit = unit;
        this.issue = issue;
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

}
