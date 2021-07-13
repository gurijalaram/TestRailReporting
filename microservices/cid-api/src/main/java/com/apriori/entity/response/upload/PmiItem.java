package com.apriori.entity.response.upload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PmiItem {
    private String name;
    private String value;
    private String type;
}
