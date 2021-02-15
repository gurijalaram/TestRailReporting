package com.apriori.apibase.services.sds;

import com.apriori.utils.http.enums.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "sds/ScenarioAvailableProcessGroupSelection.json")
public class ScenarioAvailableProcessGroupSelection {

    private String manuallyCosted;
    private String displayName;
    private String pgName;

    public String getManuallyCosted () {
        return manuallyCosted;
    }

    public void setManuallyCosted (String manuallyCosted) {
        this.manuallyCosted = manuallyCosted;
    }

    public String getDisplayName () {
        return displayName;
    }

    public void setDisplayName (String displayName) {
        this.displayName = displayName;
    }

    public String getPgName () {
        return pgName;
    }

    public void setPgName (String pgName) {
        this.pgName = pgName;
    }
}
