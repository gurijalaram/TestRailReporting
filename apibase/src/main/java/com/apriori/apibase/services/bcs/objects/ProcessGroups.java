package com.apriori.apibase.services.bcs.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "bcs/CisProcessGroupsSchema.json")
public class ProcessGroups extends Pagination {
    private List<ProcessGroup> items;
    private ProcessGroups response;

    public ProcessGroups getResponse() {
        return this.response;
    }

    public ProcessGroups setResponse(ProcessGroups response) {
        this.response = response;
        return this;
    }

    public ProcessGroups setItems(List<ProcessGroup> items) {
        this.items = items;
        return this;
    }

    public List<ProcessGroup> getItems() {
        return this.items;
    }
}
