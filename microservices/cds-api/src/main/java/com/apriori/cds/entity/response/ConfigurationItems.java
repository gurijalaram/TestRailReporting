package com.apriori.cds.entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Schema(location = "ConfigurationSchema.json")
@Data
public class ConfigurationItems {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String domain;
}