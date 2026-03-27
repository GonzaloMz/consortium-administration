package com.consortium.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Represents an issue (incident or maintenance task) associated with a building.
 */
@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Building is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(nullable = false)
    private boolean commonExpense = false;

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

    public Issue() {
    }

    public Issue(Building building, String detail, boolean commonExpense) {
        this.building = building;
        this.detail = detail;
        this.commonExpense = commonExpense;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
