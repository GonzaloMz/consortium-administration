package com.consortium.admin.exception;

public class BuildingUnitNotFoundException extends RuntimeException {

    public BuildingUnitNotFoundException(Long id) {
        super("Building unit not found with id: " + id);
    }
}
