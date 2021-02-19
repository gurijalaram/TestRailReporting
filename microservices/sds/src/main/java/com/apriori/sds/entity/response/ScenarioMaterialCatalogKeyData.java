package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioMaterialCatalogKeyData.json")
public class ScenarioMaterialCatalogKeyData {

    private String first;
    private String second;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}