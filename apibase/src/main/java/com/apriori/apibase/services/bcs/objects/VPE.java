package com.apriori.apibase.services.bcs.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "CisVPESchema.json")
public class VPE extends Pagination {
    private String name;
    private Number batchesPerYear;
    private Number defaultProductionLife;
    private Integer defaultAnnualVolume;
    private VPE response;
    private List<VPE> items;
    private List<ProcessGroup> processGroupInfo;

    public List<ProcessGroup> getProcessGroupInfo() {
        return processGroupInfo;
    }

    public VPE setProcessGroupInfo(List<ProcessGroup> processGroupInfo) {
        this.processGroupInfo = processGroupInfo;
        return this;
    }

    public List<VPE> getItems() {
        return this.items;
    }

    public VPE setItems(List<VPE> items) {
        this.items = items;
        return this;
    }

    public VPE getResponse() {
        return this.response;
    }

    public VPE setResponse(VPE response) {
        this.response = response;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public VPE setName(String name) {
        this.name = name;
        return this;
    }

    public Number getBatchesPerYear() {
        return this.batchesPerYear;
    }

    public VPE setBatchesPerYear(Number batchesPerYear) {
        this.batchesPerYear = batchesPerYear;
        return this;
    }

    public Number getDefaultProductionLife() {
        return this.defaultProductionLife;
    }

    public VPE setDefaultProductionLife(Number defaultProductionLife) {
        this.defaultProductionLife = defaultProductionLife;
        return this;
    }

    public Integer getDefaultAnnualVolume() {
        return this.defaultAnnualVolume;
    }

    public VPE setDefaultAnnualVolume(Integer defaultAnnualVolume) {
        this.defaultAnnualVolume = defaultAnnualVolume;
        return this;
    }
}
