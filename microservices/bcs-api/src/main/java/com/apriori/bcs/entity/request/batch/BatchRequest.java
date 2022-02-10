package com.apriori.bcs.entity.request.batch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BatchRequest {
    private BatchProperties batch;
}
