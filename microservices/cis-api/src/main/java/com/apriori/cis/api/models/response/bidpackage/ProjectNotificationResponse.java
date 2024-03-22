package com.apriori.cis.api.models.response.bidpackage;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "ProjectNotificationResponseSchema.json")
public class ProjectNotificationResponse extends ArrayList<ProjectNotification> {
}
