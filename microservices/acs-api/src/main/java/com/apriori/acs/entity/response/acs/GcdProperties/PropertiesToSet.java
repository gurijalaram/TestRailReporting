package com.apriori.acs.entity.response.acs.GcdProperties;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertiesToSet {
    private String roughnessRz;
    private String roughness;
    private String tolerance;
    private String concentricity;
    private String circularity;
    private String flatness;
    private String parallelism;
    private String perpendicularity;
    private String profileOfSurface;
    private String runout;
    private String totalRunout;
    private String straightness;
    private String symmetry;
    private String positionTolerance;
    private String diamTolerance;
    private String minTolerance;
    private String bendAngleTolerance;
}
