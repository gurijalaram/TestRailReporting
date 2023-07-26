package com.apriori.utils.common.customer.response;




import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "DeploymentsSchema.json")
@Data
@JsonRootName("response")
public class Deployments extends Pagination {
    private List<Deployment> items;
}
