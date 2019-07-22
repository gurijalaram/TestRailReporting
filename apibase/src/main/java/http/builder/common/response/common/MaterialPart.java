package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.http.enums.Schema;

@Schema(location = "MaterialPartSchema.json")
public class MaterialPart implements PayloadJSON{

    @JsonProperty
    private Boolean isReadyForExport;

    @JsonProperty
    private Boolean isUserPart;

    @JsonProperty
    private Boolean isSaved;

    @JsonProperty
    private String identity;

    @JsonProperty
    private String lineItemIdentity;

    @JsonProperty
    private String externalId;

    @JsonProperty
    private String manufacturerPartNumber;

    @JsonProperty
    private String manufacturer;

    @JsonProperty
    private String classification;

    @JsonProperty
    private String description;

    @JsonProperty
    private Float averageCost;

    @JsonProperty
    private Float minimumCost;

    @JsonProperty
    private String availability;

    @JsonProperty
    private String dataSheetUrl;

    @JsonProperty
    private String rohs;

    @JsonProperty
    private String costingStatus;

    @JsonProperty
    private String rohsVersion;

    public Boolean getReadyForExport() {
        return isReadyForExport;
    }

    public MaterialPart setReadyForExport(Boolean readyForExport) {
        isReadyForExport = readyForExport;
        return this;
    }

    public Boolean getUserPart() {
        return isUserPart;
    }

    public MaterialPart setUserPart(Boolean userPart) {
        isUserPart = userPart;
        return this;
    }

    public Boolean getSaved() {
        return isSaved;
    }

    public MaterialPart setSaved(Boolean saved) {
        isSaved = saved;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public MaterialPart setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getLineItemIdentity() {
        return lineItemIdentity;
    }

    public MaterialPart setLineItemIdentity(String lineItemIdentity) {
        this.lineItemIdentity = lineItemIdentity;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public MaterialPart setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }

    public MaterialPart setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
        return this;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public MaterialPart setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public String getClassification() {
        return classification;
    }

    public MaterialPart setClassification(String classification) {
        this.classification = classification;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MaterialPart setDescription(String description) {
        this.description = description;
        return this;
    }

    public Float getAverageCost() {
        return averageCost;
    }

    public MaterialPart setAverageCost(Float averageCost) {
        this.averageCost = averageCost;
        return this;
    }

    public Float getMinimumCost() {
        return minimumCost;
    }

    public MaterialPart setMinimumCost(Float minimumCost) {
        this.minimumCost = minimumCost;
        return this;
    }

    public String getAvailability() {
        return availability;
    }

    public MaterialPart setAvailability(String availability) {
        this.availability = availability;
        return this;
    }

    public String getDataSheetUrl() {
        return dataSheetUrl;
    }

    public MaterialPart setDataSheetUrl(String dataSheetUrl) {
        this.dataSheetUrl = dataSheetUrl;
        return this;
    }

    public String getRohs() {
        return rohs;
    }

    public MaterialPart setRohs(String rohs) {
        this.rohs = rohs;
        return this;
    }

    public String getRohsVersion() {
        return rohsVersion;
    }

    public MaterialPart setRohsVersion(String rohsVersion) {
        this.rohsVersion = rohsVersion;
        return this;
    }

    public String getCostingStatus() {
        return costingStatus;
    }

    public MaterialPart setCostingStatus(String costingStatus) {
        this.costingStatus = costingStatus;
        return this;
    }
}
