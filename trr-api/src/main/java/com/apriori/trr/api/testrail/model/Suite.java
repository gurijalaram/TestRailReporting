package com.apriori.trr.api.testrail.model;

import com.apriori.trr.api.testrail.TestRail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.Getter;

/**
 * TestRail suite.
 */
@Data
public class Suite {

    private int id;

    @JsonView({TestRail.Suites.Add.class, TestRail.Suites.Update.class})
    private String name;

    @JsonView({TestRail.Suites.Add.class, TestRail.Suites.Update.class})
    private String description;

    private int projectId;

    @JsonProperty
    @Getter(onMethod = @__({@JsonIgnore}))
    private boolean isBaseline;

    @JsonProperty
    @Getter(onMethod = @__({@JsonIgnore}))
    private boolean isCompleted;

    @JsonProperty
    @Getter(onMethod = @__({@JsonIgnore}))
    private boolean isMaster;

    private String url;

}