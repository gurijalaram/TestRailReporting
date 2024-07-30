package com.apriori.acs.api.models.response.acs.productioninfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessGroupItemTypeTwo {
    private String pgName;
    private String displayName;
}
