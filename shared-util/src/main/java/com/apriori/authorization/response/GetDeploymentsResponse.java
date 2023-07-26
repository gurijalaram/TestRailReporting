package com.apriori.authorization.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "GetDeployments.json")
public class GetDeploymentsResponse extends Pagination {
    private List<DeploymentItem> items;
}
