package com.apriori.ach.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ApplicationDTO {
    @EqualsAndHashCode.Exclude
    private String identitiesHierarchy;

    private String applicationName;
    private String version;
    private String installation;
}
