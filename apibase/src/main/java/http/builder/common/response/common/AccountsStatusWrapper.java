package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.http.enums.Schema;

@Schema(location = "AccountsStatusWrapperSchema.json")
public class AccountsStatusWrapper {

    @JsonProperty("response")
    private AccountStatus accountStatus;

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public AccountsStatusWrapper setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
        return this;
    }
}
