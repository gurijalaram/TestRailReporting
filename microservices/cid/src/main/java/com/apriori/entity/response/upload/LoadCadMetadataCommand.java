package com.apriori.entity.response.upload;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "LoadCadMetadata.json")
public class LoadCadMetadataCommand {
    private LoadCadMetadataCommandType command;

    public LoadCadMetadataCommandType getCommand() {
        return command;
    }

    public LoadCadMetadataCommand setCommand(LoadCadMetadataCommandType command) {
        this.command = command;
        return this;
    }
}
