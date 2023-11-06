package com.apriori.acs.api.models.response.acs.availableroutings;

import lombok.Data;

@Data

public class AvailableRoutingsFourthLevel {
    private String name;
    private String displayName;
    private String plantName;
    private String processGroupName;
    private Boolean optional;
    private Boolean included;
    private Boolean overriden;
    private Boolean alternNode;
    private Double fullyBurdenedCost;
    private Double piecePartCost;
    private Double totalInvestment;
    private String costStatus;

}
