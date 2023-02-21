package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "FeatureSchema.json")
@Data
@JsonRootName("response")
public class FeatureResponse  {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String updatedAt;
    private Boolean workOrderStatusUpdatesEnabled;
}
