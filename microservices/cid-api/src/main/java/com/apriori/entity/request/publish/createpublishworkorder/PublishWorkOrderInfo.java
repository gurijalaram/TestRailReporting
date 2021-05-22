package com.apriori.entity.request.publish.createpublishworkorder;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "cid/CreatePublishWorkOrderResponse.json")
public class PublishWorkOrderInfo {
    private PublishCommand command;

    public PublishCommand getCommand() {
        return command;
    }

    public PublishWorkOrderInfo setCommand(PublishCommand command) {
        this.command = command;
        return this;
    }
}
