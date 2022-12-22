package com.apriori.acs.entity.response.acs.tolerancepolicydefaults;

import com.apriori.acs.entity.response.acs.genericclasses.GenericExtendedPropertyInfoItem;

import lombok.Data;

@Data
public class PropertyInfoMap {
    private GenericExtendedPropertyInfoItem totalRunoutOverride;
    private GenericExtendedPropertyInfoItem perpendicularityOverride;
    private GenericExtendedPropertyInfoItem symmetryOverride;
    private GenericExtendedPropertyInfoItem roughnessOverride;
    private GenericExtendedPropertyInfoItem circularityOverride;
    private GenericExtendedPropertyInfoItem minCadToleranceThreshhold;
    private GenericExtendedPropertyInfoItem toleranceMode;
    private GenericExtendedPropertyInfoItem bendAngleToleranceOverride;
    private GenericExtendedPropertyInfoItem runoutOverride;
    private GenericExtendedPropertyInfoItem flatnessOverride;
    private GenericExtendedPropertyInfoItem parallelismOverride;
    private GenericExtendedPropertyInfoItem useCadToleranceThreshhold;
    private GenericExtendedPropertyInfoItem cadToleranceReplacement;
    private GenericExtendedPropertyInfoItem straightnessOverride;
    private GenericExtendedPropertyInfoItem positionToleranceOverride;
    private GenericExtendedPropertyInfoItem profileOfSurfaceOverride;
    private GenericExtendedPropertyInfoItem roughnessRzOverride;
    private GenericExtendedPropertyInfoItem toleranceOverride;
    private GenericExtendedPropertyInfoItem diamToleranceOverride;
    private GenericExtendedPropertyInfoItem concentricityOverride;
    private GenericExtendedPropertyInfoItem cylindricityOverride;
}
