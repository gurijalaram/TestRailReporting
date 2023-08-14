package com.apriori.ats.models.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Claims {
    private String name;
    private String email;
}
