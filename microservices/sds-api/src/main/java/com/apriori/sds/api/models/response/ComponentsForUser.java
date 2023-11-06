package com.apriori.sds.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "ComponentsForUser.json")
@Data
@JsonRootName("response")
public class ComponentsForUser {
    private List<Component> componentList;
    private Pagination pagination;
}
