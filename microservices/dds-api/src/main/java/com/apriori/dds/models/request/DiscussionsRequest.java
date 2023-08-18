package com.apriori.dds.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionsRequest {
    private DiscussionsRequestParameters discussion;
}
