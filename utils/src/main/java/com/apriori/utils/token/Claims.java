package com.apriori.utils.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Claims {
    private String name;
    private String email;
}
