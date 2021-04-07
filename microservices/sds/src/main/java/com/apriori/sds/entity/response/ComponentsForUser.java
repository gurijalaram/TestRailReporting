package com.apriori.sds.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "sds/ComponentsForUser.json")
public class ComponentsForUser {
    private List<Component> componentList;
    private ComponentsForUser response;
    private Pagination pagination;

    public List<Component> getComponentsList() {
        return componentList;
    }

    public void setComponentsList(List<Component> componentList) {
        this.componentList = componentList;
    }

    public void setResponse(ComponentsForUser response) {
        this.response = response;
    }

    public ComponentsForUser getResponse() {
        return response;
    }
}
