package com.apriori.cid.api.models.request.operators;

import com.apriori.cid.api.enums.Direction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sorting {
    private String property;
    private Direction direction;
}
