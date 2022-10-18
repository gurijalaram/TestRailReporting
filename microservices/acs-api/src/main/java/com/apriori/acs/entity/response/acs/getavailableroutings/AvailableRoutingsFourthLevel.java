package com.apriori.acs.entity.response.acs.getavailableroutings;

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
