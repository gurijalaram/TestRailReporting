package com.apriori.qa.ach.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDataDTO {
    private String applicationName;
    private String version;
    private String installation;
}
