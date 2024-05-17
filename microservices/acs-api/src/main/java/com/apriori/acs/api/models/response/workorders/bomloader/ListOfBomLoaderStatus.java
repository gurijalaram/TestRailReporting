package com.apriori.acs.api.models.response.workorders.bomloader;

import java.util.List;

public class ListOfBomLoaderStatus {

    private List<BomLoaderStatusInfo> commandType;

    public List<BomLoaderStatusInfo> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<BomLoaderStatusInfo> commandType) {
        this.commandType = commandType;
    }
}
