package com.apriori.bcs.models.request.batch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BatchRequest {
    private BatchProperties batch;
}
