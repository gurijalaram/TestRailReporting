package com.apriori.apibase.services.cid.objects.cost.costworkorderstatus;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Schema(location = "cid/CostWorkOrderStatusSchema.json")
public class CostWorkOrdersInfo {

    ObjectMapper mapper = new ObjectMapper();

    @JsonProperty("examples")
    List<CostWorkOrderInfo> costWorkOrderInfos;

    public List<CostWorkOrderInfo> getCostWorkOrderInfos() {
        return costWorkOrderInfos;
    }

    public void setCostWorkOrderInfos(List<CostWorkOrderInfo> costWorkOrderInfos) {
        this.costWorkOrderInfos = costWorkOrderInfos;
    }
}
