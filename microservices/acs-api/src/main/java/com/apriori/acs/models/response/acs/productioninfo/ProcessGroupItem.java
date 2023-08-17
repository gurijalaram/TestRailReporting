package com.apriori.acs.models.response.acs.productioninfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessGroupItem {
    private String pgName;
    private Boolean manuallyCosted;
    private String displayName;
}
