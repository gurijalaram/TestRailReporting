package com.apriori.entity.response.upload;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "LoadCadMetadata.json")
public class LoadCadMetadataCommand {
    private LoadCadMetadataCommandType command;

    public LoadCadMetadataCommandType getCommand() {
        return command;
    }

    public LoadCadMetadataCommandType setCommand(LoadCadMetadataCommandType command) {
        this.command = command;
        return new LoadCadMetadataCommandType();
    }
}
