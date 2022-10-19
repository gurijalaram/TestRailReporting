package com.apriori.acs.entity.response.acs.productioninfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentPgIdentifier {
    private String first;
    private String second;
}
