package com.consortium.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BuildingRequest {

    @NotBlank(message = "Direction is required")
    @Size(max = 255, message = "Direction must not exceed 255 characters")
    private String direction;

    private Double latitude;

    private Double longitude;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    public BuildingRequest() {
    }

    public BuildingRequest(String direction, Double latitude, Double longitude, String name) {
        this.direction = direction;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
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
}
