package com.apriori.cidappapi.entity.request.operators;

import com.apriori.cidappapi.entity.enums.Direction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sorting {
    private String property;
    private Direction direction;
}
