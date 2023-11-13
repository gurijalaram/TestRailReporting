package com.apriori.sds.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "ScenarioAssociationItemsResponse.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class ScenarioAssociationsItems extends Pagination {
    private List<ScenarioAssociation> items;
}