package com.apriori.ats.entity.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetMFA {
    private String resetBy;
}