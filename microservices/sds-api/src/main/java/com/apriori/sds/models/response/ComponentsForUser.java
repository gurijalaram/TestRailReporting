package com.apriori.sds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

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
