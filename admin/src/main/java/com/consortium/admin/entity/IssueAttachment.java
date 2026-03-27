package com.consortium.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Represents a file attachment linked to an issue.
 * The {@code attachment} field stores a relative filesystem path.
 */
@Entity
@Table(name = "issue_attachments")
public class IssueAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 512, message = "Attachment path must not exceed 512 characters")
    @Column(length = 512)
    private String attachment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @Column
    private LocalDate updateDate;

    @PrePersist
    protected void onCreate() {
        if (creationDate == null) {
            creationDate = LocalDate.now();
        }
        if (updateDate == null) {
            updateDate = creationDate;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDate.now();
    }

    public IssueAttachment() {
    }

    public IssueAttachment(String attachment, Issue issue) {
        this.attachment = attachment;
        this.issue = issue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }
}
