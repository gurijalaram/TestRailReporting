package com.apriori.ach.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    private Boolean isSingleTenant;
    private String identity;
    private String name;
    private String description;
    private String installationName;
    private String installationVersion;
    private String iconUrl;
    private String url;
}