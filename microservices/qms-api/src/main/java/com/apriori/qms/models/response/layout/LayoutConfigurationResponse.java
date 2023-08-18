package com.apriori.qms.models.response.layout;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "LayoutConfigurationResponseSchema.json")
public class LayoutConfigurationResponse {
    private String identity;
    private String customerIdentity;
    private String configuration;
    private String name;
    private Boolean shareable;
}
