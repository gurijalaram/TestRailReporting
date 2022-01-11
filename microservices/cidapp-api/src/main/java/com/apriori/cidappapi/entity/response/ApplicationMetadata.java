package com.apriori.cidappapi.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ApplicationMetadata.json")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonRootName("response")
public class ApplicationMetadata {
    private CloudContext cloudContext;
    private String customerName;
    private String deploymentName;
    private String deploymentType;
    private String installationName;
    private String applicationName;
    private Integer maxCadFileSize;
    private String salesforceId;
}
