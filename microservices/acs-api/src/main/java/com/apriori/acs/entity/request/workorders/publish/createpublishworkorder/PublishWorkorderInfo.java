package com.apriori.acs.entity.request.workorders.publish.createpublishworkorder;

import com.apriori.annotations.Schema;

@Schema(location = "workorders/CreatePublishWorkorderResponse.json")
public class PublishWorkorderInfo {
    private PublishCommand command;

    public PublishCommand getCommand() {
        return command;
    }

    public PublishWorkorderInfo setCommand(PublishCommand command) {
        this.command = command;
        return this;
    }
}
