package com.apriori.acs.entity.response.acs.saveroutingselection;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SaveRoutingSelectionInputs {
    private String name;
    private String plantName;
    private String processGroupName;
    private List<SaveRoutingSelectionInputs> children;
    private Boolean alternNode;
}
