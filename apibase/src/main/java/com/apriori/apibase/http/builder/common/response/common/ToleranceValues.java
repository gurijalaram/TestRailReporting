package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "ToleranceValuesSchema.json")
public class ToleranceValues {

    @JsonProperty
    private Object propertyValueMap;

    @JsonProperty
    private Object propertyInfoMap;

    @JsonProperty
    private Double totalRunoutOverride;

    @JsonProperty
    private Double perpendicularityOverride;

    @JsonProperty
    private Double symmetryOverride;

    @JsonProperty
    private Double roughnessOverride;

    @JsonProperty
    private Double circularityOverride;

    @JsonProperty
    private Double minCadToleranceThreshhold;

    @JsonProperty
    private Double toleranceMode;

    @JsonProperty
    private Double bendAngleToleranceOverride;

    @JsonProperty
    private Double runoutOverride;

    @JsonProperty
    private Double flatnessOverride;

    @JsonProperty
    private Double parallelismOverride;

    @JsonProperty
    private Boolean useCadToleranceThreshhold;

    @JsonProperty
    private Double cadToleranceReplacement;

    @JsonProperty
    private Double straightnessOverride;

    @JsonProperty
    private Double positionToleranceOverride;

    @JsonProperty
    private Double profileOfSurfaceOverride;

    @JsonProperty
    private Double roughnessRzOverride;

    @JsonProperty
    private Double toleranceOverride;

    @JsonProperty
    private Double diamToleranceOverride;

    @JsonProperty
    private Double concentricityOverride;

    @JsonProperty
    private Double cylindricityOverride;

    public Double getTotalRunoutOverride() {
        return totalRunoutOverride;
    }

    public ToleranceValues setTotalRunoutOverride(Double totalRunoutOverride) {
        this.totalRunoutOverride = totalRunoutOverride;
        return this;
    }

    public Double getPerpendicularityOverride() {
        return perpendicularityOverride;
    }

    public ToleranceValues setPerpendicularityOverride(Double perpendicularityOverride) {
        this.perpendicularityOverride = perpendicularityOverride;
        return this;
    }

    public Double getSymmetryOerride() {
        return symmetryOverride;
    }

    public ToleranceValues setSymmetryOerride(Double symmetryOverride) {
        this.symmetryOverride = symmetryOverride;
        return this;
    }

    public Double getRoughnessOverride() {
        return roughnessOverride;
    }

    public ToleranceValues setRoughnessOverride(Double roughnessOverride) {
        this.roughnessOverride = roughnessOverride;
        return this;
    }

    public Double getCircularityOverride() {
        return circularityOverride;
    }

    public ToleranceValues setCircularityOverride(Double circularityOverride) {
        this.circularityOverride = circularityOverride;
        return this;
    }

    public Double getMinCadToleranceThreshhold() {
        return minCadToleranceThreshhold;
    }

    public ToleranceValues setMinCadToleranceThreshhold(Double minCadToleranceThreshhold) {
        this.minCadToleranceThreshhold = minCadToleranceThreshhold;
        return this;
    }

    public Double getToleranceMode() {
        return toleranceMode;
    }

    public ToleranceValues setToleranceMode(Double toleranceMode) {
        this.toleranceMode = toleranceMode;
        return this;
    }

    public Double getBendAngleToleranceOverride() {
        return bendAngleToleranceOverride;
    }

    public ToleranceValues setBendAngleToleranceOverride(Double bendAngleToleranceOverride) {
        this.bendAngleToleranceOverride = bendAngleToleranceOverride;
        return this;
    }

    public Double getRunoutOverride() {
        return runoutOverride;
    }

    public ToleranceValues setRunoutOverride(Double runoutOverride) {
        this.runoutOverride = runoutOverride;
        return this;
    }

    public Double getFlatnessOverride() {
        return flatnessOverride;
    }

    public ToleranceValues setFlatnessOverride(Double flatnessOverride) {
        this.flatnessOverride = flatnessOverride;
        return this;
    }

    public Double getParallelismOverride() {
        return parallelismOverride;
    }

    public ToleranceValues setParallelismOverride(Double parallelismOverride) {
        this.parallelismOverride = parallelismOverride;
        return this;
    }

    public Boolean getUseCadToleranceThreshhold() {
        return useCadToleranceThreshhold;
    }

    public ToleranceValues setUseCadToleranceThreshhold(Boolean useCadToleranceThreshhold) {
        this.useCadToleranceThreshhold = useCadToleranceThreshhold;
        return this;
    }

    public Double getCadToleranceReplacement() {
        return cadToleranceReplacement;
    }

    public ToleranceValues setCadToleranceReplacement(Double cadToleranceReplacement) {
        this.cadToleranceReplacement = cadToleranceReplacement;
        return this;
    }

    public Double getStraightnessOverride() {
        return straightnessOverride;
    }

    public ToleranceValues setStraightnessOverride(Double straightnessOverride) {
        this.straightnessOverride = straightnessOverride;
        return this;
    }

    public Double getPositionToleranceOverride() {
        return positionToleranceOverride;
    }

    public ToleranceValues setPositionToleranceOverride(Double positionToleranceOverride) {
        this.positionToleranceOverride = positionToleranceOverride;
        return this;
    }

    public Double getProfileOfSurfaceOverride() {
        return profileOfSurfaceOverride;
    }

    public ToleranceValues setProfileOfSurfaceOverride(Double profileOfSurfaceOverride) {
        this.profileOfSurfaceOverride = profileOfSurfaceOverride;
        return this;
    }

    public Double getRoughnessRzOverride() {
        return roughnessRzOverride;
    }

    public ToleranceValues setRoughnessRzOverride(Double roughnessRzOverride) {
        this.roughnessRzOverride = roughnessRzOverride;
        return this;
    }

    public Double getToleranceOverride() {
        return toleranceOverride;
    }

    public ToleranceValues setToleranceOverride(Double toleranceOverride) {
        this.toleranceOverride = toleranceOverride;
        return this;
    }

    public Double getDiamToleranceOverride() {
        return diamToleranceOverride;
    }

    public ToleranceValues setDiamToleranceOverride(Double diamToleranceOverride) {
        this.diamToleranceOverride = diamToleranceOverride;
        return this;
    }

    public Double getConcentricityOverride() {
        return concentricityOverride;
    }

    public ToleranceValues setConcentricityOverride(Double concentricityOverride) {
        this.concentricityOverride = concentricityOverride;
        return this;
    }

    public Double getCylindricityOverride() {
        return cylindricityOverride;
    }

    public ToleranceValues setCylindricityOverride(Double cylindricityOverride) {
        this.cylindricityOverride = cylindricityOverride;
        return this;
    }
}