package com.consortium.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Represents an expense (spent) associated with an issue.
 */
@Entity
@Table(name = "issue_spents")
public class IssueSpent extends AbstractIssue {

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

    public IssueSpent() {
    }

    public IssueSpent(BigDecimal value, String provider, Issue issue) {
        this.value = value;
        this.provider = provider;
        this.issue = issue;
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

}
