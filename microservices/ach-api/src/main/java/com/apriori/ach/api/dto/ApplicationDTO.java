package com.apriori.ach.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ApplicationDTO {
    @EqualsAndHashCode.Exclude
    private String identitiesHierarchy;

    private String applicationName;
    private String version;
    private String installation;
}
