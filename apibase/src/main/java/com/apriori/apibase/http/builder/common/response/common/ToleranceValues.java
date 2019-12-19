package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "ToleranceValuesSchema.json")
public class ToleranceValues {

    @JsonProperty
    private Object propertyValueMap;

    @JsonProperty
    private Object propertyInfoMap;

//    @JsonProperty
//    private Integer totalRunoutOverride;
//
//    @JsonProperty
//    private Integer perpendicularityOverride;
//
//    @JsonProperty
//    private Integer symmetryOverride;
//
//    @JsonProperty
//    private Integer roughnessOverride;
//
//    @JsonProperty
//    private Integer circularityOverride;
//
//    @JsonProperty
//    private Integer minCadToleranceThreshhold;
//
//    @JsonProperty
//    private Integer toleranceMode;
//
//    @JsonProperty
//    private Integer bendAngleToleranceOverride;
//
//    @JsonProperty
//    private Integer runoutOverride;
//
    @JsonProperty
    private Integer flatnessOverride;
//
//    @JsonProperty
//    private Integer parallelismOverride;
//
//    @JsonProperty
//    private Boolean useCadToleranceThreshhold;
//
//    @JsonProperty
//    private Integer cadToleranceReplacement;
//
//    @JsonProperty
//    private Integer straightnessOverride;
//
//    @JsonProperty
//    private Integer positionToleranceOverride;
//
//    @JsonProperty
//    private Integer profileOfSurfaceOverride;
//
//    @JsonProperty
//    private Integer roughnessRzOverride;

    @JsonProperty
    private Integer toleranceOverride;
//
//    @JsonProperty
//    private Integer diamToleranceOverride;
//
//    @JsonProperty
//    private Integer concentricityOverride;
//
//    @JsonProperty
//    private Integer cylindricityOverride;

//    public Integer getTotalRunoutOverride() {
//        return totalRunoutOverride;
//    }
//
//    public void setTotalRunoutOverride(Integer totalRunoutOverride) {
//        this.totalRunoutOverride = totalRunoutOverride;
//    }
//
//    public Integer getPerpendicularityOverride() {
//        return perpendicularityOverride;
//    }
//
//    public void setPerpendicularityOverride(Integer perpendicularityOverride) {
//        this.perpendicularityOverride = perpendicularityOverride;
//    }
//
//    public Integer getSymmetryOerride() {
//        return symmetryOverride;
//    }
//
//    public void setSymmetryOerride(Integer symmetryOverride) {
//        this.symmetryOverride = symmetryOverride;
//    }
//
//    public Integer getRoughnessOverride() {
//        return roughnessOverride;
//    }
//
//    public void setRoughnessOverride(Integer roughnessOverride) {
//        this.roughnessOverride = roughnessOverride;
//    }
//
//    public Integer getCircularityOverride() {
//        return circularityOverride;
//    }
//
//    public void setCircularityOverride(Integer circularityOverride) {
//        this.circularityOverride = circularityOverride;
//    }
//
//    public Integer getMinCadToleranceThreshhold() {
//        return minCadToleranceThreshhold;
//    }
//
//    public void setMinCadToleranceThreshhold(Integer minCadToleranceThreshhold) {
//        this.minCadToleranceThreshhold = minCadToleranceThreshhold;
//    }
//
//    public Integer getToleranceMode() {
//        return toleranceMode;
//    }
//
//    public void setToleranceMode(Integer toleranceMode) {
//        this.toleranceMode = toleranceMode;
//    }
//
//    public Integer getBendAngleToleranceOverride() {
//        return bendAngleToleranceOverride;
//    }
//
//    public void setBendAngleToleranceOverride(Integer bendAngleToleranceOverride) {
//        this.bendAngleToleranceOverride = bendAngleToleranceOverride;
//    }
//
//    public Integer getRunoutOverride() {
//        return runoutOverride;
//    }
//
//    public void setRunoutOverride(Integer runoutOverride) {
//        this.runoutOverride = runoutOverride;
//    }
//
    public Integer getFlatnessOverride() {
        return flatnessOverride;
    }

    public void setFlatnessOverride(Integer flatnessOverride) {
        this.flatnessOverride = flatnessOverride;
    }
//
//    public Integer getParallelismOverride() {
//        return parallelismOverride;
//    }
//
//    public void setParallelismOverride(Integer parallelismOverride) {
//        this.parallelismOverride = parallelismOverride;
//    }
//
//    public Boolean getUseCadToleranceThreshhold() {
//        return useCadToleranceThreshhold;
//    }
//
//    public void setUseCadToleranceThreshhold(Boolean useCadToleranceThreshhold) {
//        this.useCadToleranceThreshhold = useCadToleranceThreshhold;
//    }
//
//    public Integer getCadToleranceReplacement() {
//        return cadToleranceReplacement;
//    }
//
//    public void setCadToleranceReplacement(Integer cadToleranceReplacement) {
//        this.cadToleranceReplacement = cadToleranceReplacement;
//    }
//
//    public Integer getStraightnessOverride() {
//        return straightnessOverride;
//    }
//
//    public void setStraightnessOverride(Integer straightnessOverride) {
//        this.straightnessOverride = straightnessOverride;
//    }
//
//    public Integer getPositionToleranceOverride() {
//        return positionToleranceOverride;
//    }
//
//    public void setPositionToleranceOverride(Integer positionToleranceOverride) {
//        this.positionToleranceOverride = positionToleranceOverride;
//    }
//
//    public Integer getProfileOfSurfaceOverride() {
//        return profileOfSurfaceOverride;
//    }
//
//    public void setProfileOfSurfaceOverride(Integer profileOfSurfaceOverride) {
//        this.profileOfSurfaceOverride = profileOfSurfaceOverride;
//    }
//
//    public Integer getRoughnessRzOverride() {
//        return roughnessRzOverride;
//    }
//
//    public void setRoughnessRzOverride(Integer roughnessRzOverride) {
//        this.roughnessRzOverride = roughnessRzOverride;
//    }

    public Integer getToleranceOverride() {
        return toleranceOverride;
    }

//    public void setToleranceOverride(Integer toleranceOverride) {
//        this.toleranceOverride = toleranceOverride;
//    }
//
//    public Integer getDiamToleranceOverride() {
//        return diamToleranceOverride;
//    }
//
//    public void setDiamToleranceOverride(Integer diamToleranceOverride) {
//        this.diamToleranceOverride = diamToleranceOverride;
//    }
//
//    public Integer getConcentricityOverride() {
//        return concentricityOverride;
//    }
//
//    public void setConcentricityOverride(Integer concentricityOverride) {
//        this.concentricityOverride = concentricityOverride;
//    }
//
//    public Integer getCylindricityOverride() {
//        return cylindricityOverride;
//    }
//
//    public void setCylindricityOverride(Integer cylindricityOverride) {
//        this.cylindricityOverride = cylindricityOverride;
//    }
}
