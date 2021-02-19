package com.apriori.sds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "sds/ComponentsForUser.json")
public class ComponentsForUser {
    private List<Components> componentsList;
    private ComponentsForUser response;
    private Pagination pagination;

    public List<Components> getComponentsList() {
        return componentsList;
    }

    public void setComponentsList(List<Components> componentsList) {
        this.componentsList = componentsList;
    }

    public void setResponse(ComponentsForUser response) {
        this.response = response;
    }

    public ComponentsForUser getResponse() {
        return response;
    }
}
