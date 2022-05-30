package com.apriori.utils.authorization;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(location = "ApplicationMetadata.json")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonRootName("response")
public class ApplicationMetadata {
    private CloudContext cloudContext;
    private String deploymentName;
    private String deploymentType;
    private String installationName;
    private String applicationIdentity;
    private String applicationName;
    private CustomerMetadata customerMetadata;
}
