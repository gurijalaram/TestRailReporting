package com.apriori.acs.entity.response.acs.availableroutings;

import com.apriori.annotations.Schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(location = "acs/AvailableRoutingsResponse.json")
@NoArgsConstructor
public class AvailableRoutingsFirstLevel {
    private String name;
    private String displayName;
    private String plantName;
    private String processGroupName;
    private Boolean optional;
    private Boolean included;
    private Boolean overriden;
    private Boolean alternNode;
    private List<AvailableRoutingsSecondLevel> children;

}
