package com.apriori.trr.api.testrail.model;

import com.apriori.trr.api.testrail.TestRail;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

/**
 * TestRail section.
 */
@Data
public class Section {

    private int id;

    @JsonView({TestRail.Sections.Add.class, TestRail.Sections.Update.class})
    private String name;

    @JsonView({TestRail.Sections.Add.class, TestRail.Sections.Update.class})
    private String description;

    @JsonView(TestRail.Sections.Add.class)
    private Integer suiteId;

    @JsonView(TestRail.Sections.Add.class)
    private Integer parentId;

    private int depth;

    private int displayOrder;

}
