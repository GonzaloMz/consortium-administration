package com.consortium.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


/**
 * Represents an issue (incident or maintenance task) associated with a building.
 */
@Entity
@Table(name = "issues")
public class Issue extends AbstractIssue{

    @NotNull(message = "Building is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(nullable = false)
    private boolean commonExpense = false;

    public Issue() {
    }

    public Issue(Building building, String detail, boolean commonExpense) {
        this.building = building;
        this.detail = detail;
        this.commonExpense = commonExpense;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isCommonExpense() {
        return commonExpense;
    }

    public void setCommonExpense(boolean commonExpense) {
        this.commonExpense = commonExpense;
    }

}
