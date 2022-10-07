package com.apriori.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "DeploymentsSchema.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("response")
public class Deployments extends Pagination {
    private List<Deployment> items;
}
