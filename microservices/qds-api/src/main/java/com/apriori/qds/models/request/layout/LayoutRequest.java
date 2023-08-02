package com.apriori.qds.models.request.layout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LayoutRequest {
    LayoutRequestParameters layout;
}

