package com.consortium.admin.dto;

import com.consortium.admin.entity.BuildingUnit;

public class BuildingUnitResponse {

    private Long id;
    private Long buildingId;
    private int number;
    private Long ownerId;
    private String ownerName;
    private Double coefficient;

    public BuildingUnitResponse() {
    }

    public BuildingUnitResponse(Long id, Long buildingId, int number, Long ownerId,
                                String ownerName, Double coefficient) {
        this.id = id;
        this.buildingId = buildingId;
        this.number = number;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.coefficient = coefficient;
    }

    public static BuildingUnitResponse from(BuildingUnit unit) {
        return new BuildingUnitResponse(
                unit.getId(),
                unit.getBuilding().getId(),
                unit.getNumber(),
                unit.getOwner().getId(),
                unit.getOwner().getName(),
                unit.getCoefficient()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }
}
