package com.apriori.acs.entity.response.getsettolerancepolicydefaults;

import lombok.Data;

@Data
public class PropertyInfoMap {
    private PropertyInfoItem totalRunoutOverride;
    private PropertyInfoItem perpendicularityOverride;
    private PropertyInfoItem symmetryOverride;
    private PropertyInfoItem roughnessOverride;
    private PropertyInfoItem circularityOverride;
    private PropertyInfoItem minCadToleranceThreshhold;
    private PropertyInfoItem toleranceMode;
    private PropertyInfoItem bendAngleToleranceOverride;
    private PropertyInfoItem runoutOverride;
    private PropertyInfoItem flatnessOverride;
    private PropertyInfoItem parallelismOverride;
    private PropertyInfoItem useCadToleranceThreshhold;
    private PropertyInfoItem cadToleranceReplacement;
    private PropertyInfoItem straightnessOverride;
    private PropertyInfoItem positionToleranceOverride;
    private PropertyInfoItem profileOfSurfaceOverride;
    private PropertyInfoItem roughnessRzOverride;
    private PropertyInfoItem toleranceOverride;
    private PropertyInfoItem diamToleranceOverride;
    private PropertyInfoItem concentricityOverride;
    private PropertyInfoItem cylindricityOverride;
}
