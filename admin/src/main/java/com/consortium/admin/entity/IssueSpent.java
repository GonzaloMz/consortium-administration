package com.consortium.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents an expense (spent) associated with an issue.
 */
@Entity
@Table(name = "issue_spents")
public class IssueSpent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Value is required")
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal value;

    @Size(max = 255, message = "Provider must not exceed 255 characters")
    @Column
    private String provider;

    @NotNull(message = "Issue is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_id", nullable = false)
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

    public IssueSpent() {
    }

    public IssueSpent(BigDecimal value, String provider, Issue issue) {
        this.value = value;
        this.provider = provider;
        this.issue = issue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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
