package com.apriori.cis.api.models.response.bidpackage;

import lombok.Data;

@Data
public class ProjectItemNotification {
    public String projectItemIdentity;
    public Integer unreadNotificationsCount;
}
