package com.apriori.cds.models.response;

import com.apriori.annotations.Schema;

import lombok.Data;

@Schema(location = "ConfigurationSchema.json")
@Data
public class ConfigurationItems {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String domain;
}