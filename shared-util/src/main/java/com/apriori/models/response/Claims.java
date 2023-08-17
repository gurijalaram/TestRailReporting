package com.apriori.models.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Claims {
    private String name;
    private String email;
}
