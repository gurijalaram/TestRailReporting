package com.apriori.acs.api.models.response.workorders.getadmininfo;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "workorders/AdminInfoSchema.json")
public class AdminInfoResponse {
    private String lastModifiedBy;
    private String lastSavedTime;
    private String description;
    private String status;
    private String costMaturity;
    private String comments;
    private String locked;
    private String active;
    private String lastModifiedByFullName;
}