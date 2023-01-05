package com.apriori.qms.entity.request.layout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LayoutConfigurationRequest {
    private LayoutConfigurationParameters layoutConfiguration;
}
