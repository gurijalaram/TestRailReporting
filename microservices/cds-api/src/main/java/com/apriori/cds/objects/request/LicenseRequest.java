package com.apriori.cds.objects.request;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(location = "LicenseRequestSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LicenseRequest {
    private License license;
}
