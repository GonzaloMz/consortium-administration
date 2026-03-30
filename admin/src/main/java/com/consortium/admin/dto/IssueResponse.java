package com.consortium.admin.dto;

import com.consortium.admin.entity.Issue;
import com.consortium.admin.entity.IssueAttachment;
import com.consortium.admin.entity.IssueSpent;
import com.consortium.admin.entity.IssueUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class IssueResponse {

    private Long id;
    private Long buildingId;
    private String detail;
    private boolean commonExpense;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private List<AttachmentItem> attachments;
    private List<UnitItem> units;
    private List<SpentItem> spents;

    public IssueResponse() {
    }

    public static IssueResponse from(Issue issue, List<IssueAttachment> attachments,
                                     List<IssueUnit> units, List<IssueSpent> spents) {
        IssueResponse response = new IssueResponse();
        response.id = issue.getId();
        response.buildingId = issue.getBuilding().getId();
        response.detail = issue.getDetail();
        response.commonExpense = issue.isCommonExpense();
        response.creationDate = issue.getCreationDate();
        response.updateDate = issue.getUpdateDate();
        response.attachments = attachments.stream()
                .map(a -> new AttachmentItem(a.getId(), a.getAttachment()))
                .collect(Collectors.toList());
        response.units = units.stream()
                .map(u -> new UnitItem(u.getId(), u.getUnit().getId(), u.getUnit().getNumber()))
                .collect(Collectors.toList());
        response.spents = spents.stream()
                .map(s -> new SpentItem(s.getId(), s.getValue(), s.getProvider()))
                .collect(Collectors.toList());
        return response;
    }

    // --- Getters / setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBuildingId() { return buildingId; }
    public void setBuildingId(Long buildingId) { this.buildingId = buildingId; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public boolean isCommonExpense() { return commonExpense; }
    public void setCommonExpense(boolean commonExpense) { this.commonExpense = commonExpense; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }

    public List<AttachmentItem> getAttachments() { return attachments; }
    public void setAttachments(List<AttachmentItem> attachments) { this.attachments = attachments; }

    public List<UnitItem> getUnits() { return units; }
    public void setUnits(List<UnitItem> units) { this.units = units; }

    public List<SpentItem> getSpents() { return spents; }
    public void setSpents(List<SpentItem> spents) { this.spents = spents; }

    // --- Nested item types ---

    public static class AttachmentItem {
        private Long id;
        private String attachment;

        public AttachmentItem() {}
        public AttachmentItem(Long id, String attachment) {
            this.id = id;
            this.attachment = attachment;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getAttachment() { return attachment; }
        public void setAttachment(String attachment) { this.attachment = attachment; }
    }

    public static class UnitItem {
        private Long id;
        private Long unitId;
        private int unitNumber;

        public UnitItem() {}
        public UnitItem(Long id, Long unitId, int unitNumber) {
            this.id = id;
            this.unitId = unitId;
            this.unitNumber = unitNumber;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUnitId() { return unitId; }
        public void setUnitId(Long unitId) { this.unitId = unitId; }
        public int getUnitNumber() { return unitNumber; }
        public void setUnitNumber(int unitNumber) { this.unitNumber = unitNumber; }
    }

    public static class SpentItem {
        private Long id;
        private BigDecimal value;
        private String provider;

        public SpentItem() {}
        public SpentItem(Long id, BigDecimal value, String provider) {
            this.id = id;
            this.value = value;
            this.provider = provider;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public BigDecimal getValue() { return value; }
        public void setValue(BigDecimal value) { this.value = value; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
    }
}
