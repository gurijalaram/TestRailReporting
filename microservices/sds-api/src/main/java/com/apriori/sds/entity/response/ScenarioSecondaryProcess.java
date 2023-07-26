package com.apriori.sds.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "ScenarioSecondaryProcesses.json")
@Data
@JsonRootName("response")
public class ScenarioSecondaryProcess {
    private String name;
    private String displayName;
    private String plantName;
    private String processGroupName;
    private List<ScenarioSecondaryProcess> children;
    private Boolean optional;
    private Boolean included;
    private Boolean overriden;
    private Boolean alternNode;
    private Integer includedChildrenCount;
    private Boolean leafNode;
}
