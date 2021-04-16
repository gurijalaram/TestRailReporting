package com.apriori.sds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "sds/ComponentsForUser.json")
@Data
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentsForUser {
    private List<Component> componentList;
    private Pagination pagination;
}
