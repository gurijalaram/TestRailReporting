package com.apriori.trr.api.testrail.model;

import com.apriori.trr.api.testrail.TestRail;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * TestRail milestone.
 */
@Data
public class Milestone {

    private int id;

    @JsonView({TestRail.Milestones.Add.class, TestRail.Milestones.Update.class})
    private String name;

    @JsonView({TestRail.Milestones.Add.class, TestRail.Milestones.Update.class})
    private String description;

    private int projectId;

    @JsonView({TestRail.Milestones.Add.class, TestRail.Milestones.Update.class})
    private Date dueOn;

    @JsonView({TestRail.Milestones.Update.class})
    @Getter(value = AccessLevel.PRIVATE)
    @Setter(value = AccessLevel.PRIVATE)
    private Boolean isCompleted;

    public Boolean isCompleted() {
        return getIsCompleted();
    }

    private Date completedOn;

    private String url;
}
