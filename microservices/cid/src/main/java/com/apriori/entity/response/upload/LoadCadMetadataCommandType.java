package com.apriori.entity.response.upload;

import com.apriori.entity.request.cost.createcostworkorder.CostOrderCommandType;
import com.apriori.entity.request.cost.createcostworkorder.CostOrderInputs;
import com.apriori.entity.response.cost.costworkorderstatus.CostOrderStatusInputs;
import com.apriori.entity.response.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.entity.response.cost.costworkorderstatus.CostStatusCommand;

public class LoadCadMetadataCommandType {
    private String commandType;
    private LoadCadMetadataInputs inputs;

    public String getCommandType() {
        return commandType;
    }

    public LoadCadMetadataCommandType setCommandType(String commandType) {
        this.commandType = commandType;
        return this;
    }

    public LoadCadMetadataInputs getInputs() {
        return inputs;
    }

    public LoadCadMetadataCommandType setInputs(LoadCadMetadataInputs inputs) {
        this.inputs = inputs;
        return this;
    }
}
