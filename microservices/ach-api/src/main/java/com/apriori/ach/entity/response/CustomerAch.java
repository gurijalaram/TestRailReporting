package com.apriori.ach.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CustomerAchSchema.json")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("response")
public class CustomerAch {
    private String identity;
    private String name;
    private List<DeploymentAch> deployments;
}