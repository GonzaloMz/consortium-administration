package com.consortium.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


/**
 * Represents a file attachment linked to an issue.
 * The {@code attachment} field stores a relative filesystem path.
 */
@Entity
@Table(name = "issue_attachments")
public class IssueAttachment extends AbstractIssue{

    @Size(max = 512, message = "Attachment path must not exceed 512 characters")
    @Column(length = 512)
    private String attachment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    public IssueAttachment() {
    }

    public IssueAttachment(String attachment, Issue issue) {
        this.attachment = attachment;
        this.issue = issue;
    }


    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

}
