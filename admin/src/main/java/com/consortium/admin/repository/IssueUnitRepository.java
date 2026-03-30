package com.consortium.admin.repository;

import com.consortium.admin.entity.IssueUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueUnitRepository extends JpaRepository<IssueUnit, Long> {

    List<IssueUnit> findAllByIssueId(Long issueId);
}
