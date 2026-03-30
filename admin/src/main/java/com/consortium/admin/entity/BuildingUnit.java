package com.consortium.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a unit (apartment/flat) within a building.
 * The {@code number} is a sequence starting from 1 for each building.
 */
@Entity
@Table(name = "building_units")
public class BuildingUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Building is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(nullable = false)
    private int number;

    @NotNull(message = "Owner is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Person owner;

    @NotNull(message = "Coefficient is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Coefficient must be greater than 0")
    @DecimalMax(value = "1.0", inclusive = false, message = "Coefficient must be less than 1")
    @Column(nullable = false)
    private Double coefficient;

    public BuildingUnit() {
    }

    public BuildingUnit(Building building, int number, Person owner, Double coefficient) {
        this.building = building;
        this.number = number;
        this.owner = owner;
        this.coefficient = coefficient;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }
}
