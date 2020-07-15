package com.apriori.apibase.services.cid.objects.upload;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "FileOrderSchema.json")
public class FileCommandEntity {

    @JsonProperty
    private FileOrdersEntity command;

    public FileOrdersEntity getCommand() {
        return command;
    }

    public FileCommandEntity setCommand(FileOrdersEntity command) {
        this.command = command;
        return this;
    }
}
