package com.consortium.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class IssueAttachmentRequest {

    @NotBlank(message = "Attachment path is required")
    @Size(max = 512, message = "Attachment path must not exceed 512 characters")
    private String attachment;

    public IssueAttachmentRequest() {
    }

    public IssueAttachmentRequest(String attachment) {
        this.attachment = attachment;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
