package com.consortium.admin.dto;

import jakarta.validation.constraints.NotNull;

public class IssueUnitRequest {

    @NotNull(message = "Unit ID is required")
    private Long unitId;

    public IssueUnitRequest() {
    }

    public IssueUnitRequest(Long unitId) {
        this.unitId = unitId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }
}
