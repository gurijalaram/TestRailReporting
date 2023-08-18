package com.apriori.acs.models.response.workorders.upload;

import com.apriori.annotations.Schema;

@Schema(location = "workorders/FileOrderSchema.json")
public class FileCommand {
    private FileOrdersUpload command;

    public FileOrdersUpload getCommand() {
        return command;
    }

    public FileCommand setCommand(FileOrdersUpload command) {
        this.command = command;
        return this;
    }
}
