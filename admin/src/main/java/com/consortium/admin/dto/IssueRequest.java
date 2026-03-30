package com.consortium.admin.dto;

import jakarta.validation.constraints.NotNull;

public class IssueRequest {

    @NotNull(message = "Building ID is required")
    private Long buildingId;

    private String detail;

    private boolean commonExpense = false;

    public IssueRequest() {
    }

    public IssueRequest(Long buildingId, String detail, boolean commonExpense) {
        this.buildingId = buildingId;
        this.detail = detail;
        this.commonExpense = commonExpense;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
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
