package com.apriori.cidappapi.models.request.operators;

import com.apriori.cidappapi.enums.Direction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sorting {
    private String property;
    private Direction direction;
}
