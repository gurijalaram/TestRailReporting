package com.apriori.ach.models.response;

import lombok.Data;

import java.util.List;

@Data
public class CustomerAssignedRoles {
    private String identity;
    private String name;
    private List<AssignedApplication> applications;
    private List<RequiredUserProperties> requiredUserProperties = null;
}