package com.apriori.ach.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "CustomerAssignedRolesSchema.json")
@Data
@JsonRootName("response")
public class EnablementsSupport {
    private List<CustomerAssignedRoles> customerAssignedRoles;
}