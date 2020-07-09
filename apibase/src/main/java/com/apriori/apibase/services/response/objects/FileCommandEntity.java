package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "FileOrderSchema.json")
public class FileCommandEntity {

    @JsonProperty
    private List<FileOrdersEntity> command;

    public List<FileOrdersEntity> getCommand() {
        return command;
    }

    public FileCommandEntity setCommand(List<FileOrdersEntity> command) {
        this.command = command;
        return this;
    }
}
