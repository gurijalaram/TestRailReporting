package com.apriori.qds.api.models.request.layout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewElementRequest {
    private ViewElementRequestParameters viewElement;
}
