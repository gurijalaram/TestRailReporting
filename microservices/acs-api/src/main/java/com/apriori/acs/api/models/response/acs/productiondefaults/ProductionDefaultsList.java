package com.apriori.acs.api.models.response.acs.productiondefaults;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductionDefaultsList {
    @JsonProperty("prod.info.default.material")
    private String prodInfoDefaultMaterial;
    @JsonProperty("prod.info.default.annual.volume")
    private int prodInfoDefaultAnnualVolume;
    @JsonProperty("prod.info.default.use.vpe.for.all.processes")
    private String prodInfoDefaultUseVpeForAllProcesses;
    @JsonProperty("prod.info.default.material.catalog.name")
    private String prodInfoDefaultMaterialCatalogName;
    @JsonProperty("prod.info.default.production.life")
    private String prodInfoDefaultProductionLife;
    @JsonProperty("prod.info.default.batch.size")
    private String prodInfoDefaultBatchSize;
    @JsonProperty("prod.info.default.pg")
    private String prodInfoDefaultPg;
    @JsonProperty("prod.info.default.vpe")
    private String prodInfoDefaultVpe;
}
