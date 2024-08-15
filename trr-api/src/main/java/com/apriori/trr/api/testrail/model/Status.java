package com.apriori.trr.api.testrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

/**
 * TestRail status.
 */
@Data
public class Status {

    private int id;
    private String name;
    private String label;
    private int colorDark;
    private int colorMedium;
    private int colorBright;
    @JsonProperty
    @Getter(onMethod = @__({@JsonIgnore}))
    private boolean isSystem;
    @JsonProperty
    @Getter(onMethod = @__({@JsonIgnore}))
    private boolean isUntested;
    @JsonProperty
    @Getter(onMethod = @__({@JsonIgnore}))
    private boolean isFinal;
}
