package com.consortium.admin.repository;

import com.consortium.admin.entity.BuildingUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildingUnitRepository extends JpaRepository<BuildingUnit, Long> {

    List<BuildingUnit> findAllByBuildingId(Long buildingId);

    @Query("SELECT MAX(bu.number) FROM BuildingUnit bu WHERE bu.building.id = :buildingId")
    Optional<Integer> findMaxNumberByBuildingId(@Param("buildingId") Long buildingId);
}
