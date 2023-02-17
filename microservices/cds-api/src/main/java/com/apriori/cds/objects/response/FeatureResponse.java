package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "FeatureSchema.json")
@Data
public class FeatureResponse  {
    String identity;
    String createdBy;
    String createdAt;
    Boolean workOrderStatusUpdatesEnabled;
}
