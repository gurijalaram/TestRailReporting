package com.apriori.acs.api.models.request.workorders.publish.createpublishworkorder;

import com.apriori.shared.util.annotations.Schema;

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
