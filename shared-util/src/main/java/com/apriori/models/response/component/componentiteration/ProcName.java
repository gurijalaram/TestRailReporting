package com.apriori.models.response.component.componentiteration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProcName {
    private String processGroupName;
    private String processName;
    private Integer index;
    private String displayName;
}