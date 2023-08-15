package com.apriori.ats.models.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetMFA {
    private String resetBy;
}