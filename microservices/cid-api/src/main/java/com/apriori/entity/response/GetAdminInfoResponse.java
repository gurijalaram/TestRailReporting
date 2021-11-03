package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "GetAdminInfoSchema.json")
public class GetAdminInfoResponse {
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
