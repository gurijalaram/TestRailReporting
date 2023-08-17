package com.apriori.cds.models.request;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Features;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "FeatureRequestSchema.json")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FeatureRequest {
    private Features features;
}
