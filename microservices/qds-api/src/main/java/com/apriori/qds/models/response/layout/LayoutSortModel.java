package com.apriori.qds.models.response.layout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LayoutSortModel {
    private String key;
    private String operation;
}
