package com.consortium.admin.repository;

import com.consortium.admin.entity.IssueSpent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueSpentRepository extends JpaRepository<IssueSpent, Long> {

    List<IssueSpent> findAllByIssueId(Long issueId);
}
