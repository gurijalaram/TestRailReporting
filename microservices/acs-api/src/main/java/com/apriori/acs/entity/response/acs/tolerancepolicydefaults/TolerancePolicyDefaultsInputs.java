package com.apriori.acs.entity.response.acs.tolerancepolicydefaults;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TolerancePolicyDefaultsInputs {
    private double totalRunoutOverride;
    private double perpendicularityOverride;
    private double symmetryOverride;
    private double roughnessOverride;
    private double circularityOverride;
    private double minCadToleranceThreshhold;
    private String toleranceMode;
    private double bendAngleToleranceOverride;
    private double runoutOverride;
    private double flatnessOverride;
    private double parallelismOverride;
    private boolean useCadToleranceThreshhold;
    private double cadToleranceReplacement;
    private double straightnessOverride;
    private double positionToleranceOverride;
    private double profileOfSurfaceOverride;
    private double roughnessRzOverride;
    private double toleranceOverride;
    private double diamToleranceOverride;
    private double concentricityOverride;
    private double cylindricityOverride;
}
