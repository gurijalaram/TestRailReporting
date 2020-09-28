package com.apriori.apibase.services.cis.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "cds/CdsCustomersSchema.json")
public class CisCustomers extends Pagination {
    private List<CisCustomer> items;
    private CisCustomers response;

    public CisCustomers getResponse() {
        return this.response;
    }

    public CisCustomers setResponse(CisCustomers response) {
        this.response = response;
        return this;
    }

    public CisCustomers setItems(List<CisCustomer> items) {
        this.items = items;
        return this;
    }

    public List<CisCustomer> getItems() {
        return this.items;
    }

}
