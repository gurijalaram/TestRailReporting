package com.apriori.acs.entity.response.getsetproductiondefaults;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductionDefaultsList {
    @JsonProperty("prod.info.default.material")
    private String prodinfodefaultmaterial;
    @JsonProperty("prod.info.default.annual.volume")
    private int prodinfodefaultannualvolume;
    @JsonProperty("prod.info.default.use.vpe.for.all.processes")
    private String prodinfodefaultusevpeforallprocesses;
    @JsonProperty("prod.info.default.material.catalog.name")
    private String prodinfodefaultmaterialcatalogname;
    @JsonProperty("prod.info.default.production.life")
    private String prodinfodefaultproductionlife;
    @JsonProperty("prod.info.default.batch.size")
    private String prodinfodefaultbatchsize;
    @JsonProperty("prod.info.default.pg")
    private String prodinfodefaultpg;
    @JsonProperty("prod.info.default.vpe")
    private String prodinfodefaultvpe;
}
