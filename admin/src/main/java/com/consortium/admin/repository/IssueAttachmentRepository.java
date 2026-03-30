package com.consortium.admin.repository;

import com.consortium.admin.entity.IssueAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueAttachmentRepository extends JpaRepository<IssueAttachment, Long> {

    List<IssueAttachment> findAllByIssueId(Long issueId);
}
