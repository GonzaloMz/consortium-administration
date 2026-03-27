package com.consortium.admin.repository;

import com.consortium.admin.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

    List<Building> findAllByDeletedFalse();

    Optional<Building> findByIdAndDeletedFalse(Long id);
}
