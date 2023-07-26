package com.apriori.apibase.services.response.objects;



import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "ToleranceValuesSchema.json")
public class ToleranceValuesEntity {

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
    private String toleranceMode;

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

    public ToleranceValuesEntity setTotalRunoutOverride(Double totalRunoutOverride) {
        this.totalRunoutOverride = totalRunoutOverride;
        return this;
    }

    public Double getPerpendicularityOverride() {
        return perpendicularityOverride;
    }

    public ToleranceValuesEntity setPerpendicularityOverride(Double perpendicularityOverride) {
        this.perpendicularityOverride = perpendicularityOverride;
        return this;
    }

    public Double getSymmetryOverride() {
        return symmetryOverride;
    }

    public ToleranceValuesEntity setSymmetryOverride(Double symmetryOverride) {
        this.symmetryOverride = symmetryOverride;
        return this;
    }

    public Double getRoughnessOverride() {
        return roughnessOverride;
    }

    public ToleranceValuesEntity setRoughnessOverride(Double roughnessOverride) {
        this.roughnessOverride = roughnessOverride;
        return this;
    }

    public Double getCircularityOverride() {
        return circularityOverride;
    }

    public ToleranceValuesEntity setCircularityOverride(Double circularityOverride) {
        this.circularityOverride = circularityOverride;
        return this;
    }

    public Double getMinCadToleranceThreshhold() {
        return minCadToleranceThreshhold;
    }

    public ToleranceValuesEntity setMinCadToleranceThreshhold(Double minCadToleranceThreshhold) {
        this.minCadToleranceThreshhold = minCadToleranceThreshhold;
        return this;
    }

    public String getToleranceMode() {
        return toleranceMode;
    }

    public ToleranceValuesEntity setToleranceMode(String toleranceMode) {
        this.toleranceMode = toleranceMode;
        return this;
    }

    public Double getBendAngleToleranceOverride() {
        return bendAngleToleranceOverride;
    }

    public ToleranceValuesEntity setBendAngleToleranceOverride(Double bendAngleToleranceOverride) {
        this.bendAngleToleranceOverride = bendAngleToleranceOverride;
        return this;
    }

    public Double getRunoutOverride() {
        return runoutOverride;
    }

    public ToleranceValuesEntity setRunoutOverride(Double runoutOverride) {
        this.runoutOverride = runoutOverride;
        return this;
    }

    public Double getFlatnessOverride() {
        return flatnessOverride;
    }

    public ToleranceValuesEntity setFlatnessOverride(Double flatnessOverride) {
        this.flatnessOverride = flatnessOverride;
        return this;
    }

    public Double getParallelismOverride() {
        return parallelismOverride;
    }

    public ToleranceValuesEntity setParallelismOverride(Double parallelismOverride) {
        this.parallelismOverride = parallelismOverride;
        return this;
    }

    public Boolean getUseCadToleranceThreshhold() {
        return useCadToleranceThreshhold;
    }

    public ToleranceValuesEntity setUseCadToleranceThreshhold(Boolean useCadToleranceThreshhold) {
        this.useCadToleranceThreshhold = useCadToleranceThreshhold;
        return this;
    }

    public Double getCadToleranceReplacement() {
        return cadToleranceReplacement;
    }

    public ToleranceValuesEntity setCadToleranceReplacement(Double cadToleranceReplacement) {
        this.cadToleranceReplacement = cadToleranceReplacement;
        return this;
    }

    public Double getStraightnessOverride() {
        return straightnessOverride;
    }

    public ToleranceValuesEntity setStraightnessOverride(Double straightnessOverride) {
        this.straightnessOverride = straightnessOverride;
        return this;
    }

    public Double getPositionToleranceOverride() {
        return positionToleranceOverride;
    }

    public ToleranceValuesEntity setPositionToleranceOverride(Double positionToleranceOverride) {
        this.positionToleranceOverride = positionToleranceOverride;
        return this;
    }

    public Double getProfileOfSurfaceOverride() {
        return profileOfSurfaceOverride;
    }

    public ToleranceValuesEntity setProfileOfSurfaceOverride(Double profileOfSurfaceOverride) {
        this.profileOfSurfaceOverride = profileOfSurfaceOverride;
        return this;
    }

    public Double getRoughnessRzOverride() {
        return roughnessRzOverride;
    }

    public ToleranceValuesEntity setRoughnessRzOverride(Double roughnessRzOverride) {
        this.roughnessRzOverride = roughnessRzOverride;
        return this;
    }

    public Double getToleranceOverride() {
        return toleranceOverride;
    }

    public ToleranceValuesEntity setToleranceOverride(Double toleranceOverride) {
        this.toleranceOverride = toleranceOverride;
        return this;
    }

    public Double getDiamToleranceOverride() {
        return diamToleranceOverride;
    }

    public ToleranceValuesEntity setDiamToleranceOverride(Double diamToleranceOverride) {
        this.diamToleranceOverride = diamToleranceOverride;
        return this;
    }

    public Double getConcentricityOverride() {
        return concentricityOverride;
    }

    public ToleranceValuesEntity setConcentricityOverride(Double concentricityOverride) {
        this.concentricityOverride = concentricityOverride;
        return this;
    }

    public Double getCylindricityOverride() {
        return cylindricityOverride;
    }

    public ToleranceValuesEntity setCylindricityOverride(Double cylindricityOverride) {
        this.cylindricityOverride = cylindricityOverride;
        return this;
    }
}