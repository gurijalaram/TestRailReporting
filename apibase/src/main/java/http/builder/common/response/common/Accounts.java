package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.http.enums.Schema;

import java.util.List;

@Schema(location = "AccountsSchema.json")
public class Accounts extends BasePageResponse {

    @JsonProperty("items")
    private List<AccountStatus> accountStatuses;

    public List<AccountStatus> getAccountStatuses() {
        return accountStatuses;
    }

    public Accounts setAccountStatuses(List<AccountStatus> accountStatuses) {
        this.accountStatuses = accountStatuses;
        return this;
    }
}
