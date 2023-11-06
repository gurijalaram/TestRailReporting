package com.apriori.acs.api.models.response.workorders.upload;

import com.apriori.shared.util.annotations.Schema;

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
