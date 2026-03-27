package com.consortium.admin.dto;

import com.consortium.admin.entity.Building;

import java.time.LocalDate;

public class BuildingResponse {

    private Long id;
    private String direction;
    private Double latitude;
    private Double longitude;
    private String name;
    private LocalDate creationDate;

    public BuildingResponse() {
    }

    public BuildingResponse(Long id, String direction, Double latitude, Double longitude,
                            String name, LocalDate creationDate) {
        this.id = id;
        this.direction = direction;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.creationDate = creationDate;
    }

    public static BuildingResponse from(Building building) {
        return new BuildingResponse(
                building.getId(),
                building.getDirection(),
                building.getLatitude(),
                building.getLongitude(),
                building.getName(),
                building.getCreationDate()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
