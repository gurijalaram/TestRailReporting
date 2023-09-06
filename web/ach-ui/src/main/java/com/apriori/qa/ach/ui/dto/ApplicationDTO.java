package com.apriori.qa.ach.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Data;
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
