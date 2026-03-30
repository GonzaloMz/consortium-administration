package com.consortium.admin.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class BuildingUnitRequest {

    @NotNull(message = "Building ID is required")
    private Long buildingId;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    @NotNull(message = "Coefficient is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Coefficient must be greater than 0")
    @DecimalMax(value = "1.0", inclusive = false, message = "Coefficient must be less than 1")
    private Double coefficient;

    public BuildingUnitRequest() {
    }

    public BuildingUnitRequest(Long buildingId, Long ownerId, Double coefficient) {
        this.buildingId = buildingId;
        this.ownerId = ownerId;
        this.coefficient = coefficient;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }
}
