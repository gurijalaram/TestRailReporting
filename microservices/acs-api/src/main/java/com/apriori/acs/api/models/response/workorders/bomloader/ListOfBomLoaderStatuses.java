package com.apriori.acs.api.models.response.workorders.bomloader;

import com.apriori.shared.util.annotations.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(location = "workorders/BomLoaderSchema.json")
public class ListOfBomLoaderStatuses extends ArrayList<ListOfBomLoaderStatuses> {

    private List<ListOfBomLoaderStatus> commandType;

    public List<ListOfBomLoaderStatus> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<ListOfBomLoaderStatus> commandType) {
        this.commandType = commandType;
    }
}