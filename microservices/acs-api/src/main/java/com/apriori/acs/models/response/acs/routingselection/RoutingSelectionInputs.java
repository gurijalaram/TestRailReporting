package com.apriori.acs.models.response.acs.routingselection;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoutingSelectionInputs {
    private String name;
    private String plantName;
    private String processGroupName;
    private List<RoutingSelectionInputs> children;
    private Boolean alternNode;
}
