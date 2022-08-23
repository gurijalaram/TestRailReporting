package com.apriori.cnh.entity.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Params {
    String name;
    String value;
    String type;
}
