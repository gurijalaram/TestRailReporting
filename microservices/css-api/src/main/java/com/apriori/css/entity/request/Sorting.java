package com.apriori.css.entity.request;

import com.apriori.css.entity.enums.Direction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sorting {
    private String property;
    private Direction direction;
}
