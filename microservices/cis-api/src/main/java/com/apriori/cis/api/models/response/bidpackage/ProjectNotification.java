package com.apriori.cis.api.models.response.bidpackage;

import lombok.Data;

@Data
public class ProjectNotification {
    public String projectIdentity;
    public Integer unreadNotificationsCount;
}
