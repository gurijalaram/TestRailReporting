package com.apriori.apibase.services.cid.objects.response.upload;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "FileOrderSchema.json")
public class FileCommand {

    @JsonProperty
    private FileOrdersUpload command;

    public FileOrdersUpload getCommand() {
        return command;
    }

    public FileCommand setCommand(FileOrdersUpload command) {
        this.command = command;
        return this;
    }
}
