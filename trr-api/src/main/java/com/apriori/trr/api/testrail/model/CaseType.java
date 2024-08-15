package com.apriori.trr.api.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

/**
 * TestRail case type.
 */
@Data
public class CaseType {

    private int id;
    private String name;
    @JsonProperty
    @Getter(onMethod = @__({@JsonIgnore}))
    private boolean isDefault;

}
