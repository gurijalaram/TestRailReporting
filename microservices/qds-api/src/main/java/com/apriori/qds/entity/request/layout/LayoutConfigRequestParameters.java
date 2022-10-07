package com.apriori.qds.entity.request.layout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LayoutConfigRequestParameters {
    public String configuration;
    public String name;
    public boolean shareable;
}
