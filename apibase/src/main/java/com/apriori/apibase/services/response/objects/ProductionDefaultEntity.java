package com.apriori.apibase.services.response.objects;



import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "ProductionDefaultSchema.json")
public class ProductionDefaultEntity {

    @JsonProperty
    private Object propertyValueMap;

    @JsonProperty
    private Object propertyInfoMap;

    @JsonProperty
    private String pg;

    @JsonProperty
    private String vpe;

    @JsonProperty
    private String material;

    @JsonProperty
    private String materialCatalogName;

    @JsonProperty
    private Double annualVolume;

    @JsonProperty
    private Double productionLife;

    @JsonProperty
    private Double batchSize;

    @JsonProperty
    private Boolean useVpeForAllProcesses;

    @JsonProperty
    private String defaults;

    @JsonProperty
    private Boolean batchSizeMode;

    public String getPg() {
        return pg;
    }

    public ProductionDefaultEntity setPg(String pg) {
        this.pg = pg;
        return this;
    }

    public String getVpe() {
        return vpe;
    }

    public ProductionDefaultEntity setVpe(String vpe) {
        this.vpe = vpe;
        return this;
    }

    public String getMaterial() {
        return material;
    }

    public ProductionDefaultEntity setMaterial(String material) {
        this.material = material;
        return this;
    }

    public String getMaterialCatalogName() {
        return materialCatalogName;
    }

    public ProductionDefaultEntity setMaterialCatalogName(String materialCatalogName) {
        this.materialCatalogName = materialCatalogName;
        return this;
    }

    public Number getAnnualVolume() {
        return annualVolume;
    }

    public ProductionDefaultEntity setAnnualVolume(Double annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    public Number getProductionLife() {
        return productionLife;
    }

    public ProductionDefaultEntity setProductionLife(Double productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    public Number getBatchSize() {
        return batchSize;
    }

    public ProductionDefaultEntity setBatchSize(Double batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public Boolean getUseVpeForAllProcesses() {
        return useVpeForAllProcesses;
    }

    public ProductionDefaultEntity setUseVpeForAllProcesses(Boolean useVpeForAllProcesses) {
        this.useVpeForAllProcesses = useVpeForAllProcesses;
        return this;
    }

    public String getDefaults() {
        return defaults;
    }

    public ProductionDefaultEntity setDefaults(String defaults) {
        this.defaults = defaults;
        return this;
    }

    public Boolean isBatchSizeMode() {
        return batchSizeMode;
    }

    public ProductionDefaultEntity setBatchSizeMode(Boolean batchSizeMode) {
        this.batchSizeMode = batchSizeMode;
        return this;
    }

}