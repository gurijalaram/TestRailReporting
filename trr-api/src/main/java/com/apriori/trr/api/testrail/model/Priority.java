package com.apriori.trr.api.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

/**
 * TestRail test case priority.
 */
@Data
public class Priority {

    private int id;
    private String name;
    private String shortName;
    private int priority;
    @JsonProperty
    @Getter(onMethod = @__({@JsonIgnore}))
    private boolean isDefault;

}
