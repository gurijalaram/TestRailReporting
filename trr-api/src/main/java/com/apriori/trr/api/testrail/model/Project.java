package com.apriori.trr.api.testrail.model;

import com.apriori.trr.api.testrail.TestRail;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * TestRail project.
 */
@Data
public class Project {

    private int id;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    private String name;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    private String announcement;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    @Getter(value = AccessLevel.PRIVATE)
    private Boolean showAnnouncement;

    @JsonView(TestRail.Projects.Update.class)
    @Getter(value = AccessLevel.PRIVATE)
    @Setter(value = AccessLevel.PRIVATE)
    private Boolean isCompleted;

    private Date completedOn;

    private String url;

    @JsonView({TestRail.Projects.Add.class, TestRail.Projects.Update.class})
    private Integer suiteMode;

    public Boolean isCompleted() {
        return getIsCompleted();
    }

    public Boolean isShowAnnouncement() {
        return getShowAnnouncement();
    }

}
