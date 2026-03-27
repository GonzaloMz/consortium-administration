package com.consortium.admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class IssueSpentRequest {

    @NotNull(message = "Value is required")
    private BigDecimal value;

    @Size(max = 255, message = "Provider must not exceed 255 characters")
    private String provider;

    public IssueSpentRequest() {
    }

    public IssueSpentRequest(BigDecimal value, String provider) {
        this.value = value;
        this.provider = provider;
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
}
