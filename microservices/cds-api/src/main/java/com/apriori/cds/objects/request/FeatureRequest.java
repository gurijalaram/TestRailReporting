package com.apriori.cds.objects.request;

import com.apriori.annotations.Schema;

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
