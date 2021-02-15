package com.apriori.apibase.services.sds;

import com.apriori.utils.http.enums.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "sds/ScenarioMaterialBean.json")
public class ScenarioMaterialBean {

    private String isCadMaterialNameValid;
    private String utilizationMode;
    private String initialized;
    private String materialMode;
    private String isUserMaterialNameValid;
    private String vpeDefaultMaterialName;
    private String stockMode;
    private String userUtilizationOverride;
    private String userMaterialName;

    public String getIsCadMaterialNameValid () {
        return isCadMaterialNameValid;
    }

    public void setIsCadMaterialNameValid (String isCadMaterialNameValid) {
        this.isCadMaterialNameValid = isCadMaterialNameValid;
    }

    public String getUtilizationMode () {
        return utilizationMode;
    }

    public void setUtilizationMode (String utilizationMode) {
        this.utilizationMode = utilizationMode;
    }

    public String getInitialized () {
        return initialized;
    }

    public void setInitialized (String initialized) {
        this.initialized = initialized;
    }

    public String getMaterialMode () {
        return materialMode;
    }

    public void setMaterialMode (String materialMode) {
        this.materialMode = materialMode;
    }

    public String getIsUserMaterialNameValid () {
        return isUserMaterialNameValid;
    }

    public void setIsUserMaterialNameValid (String isUserMaterialNameValid) {
        this.isUserMaterialNameValid = isUserMaterialNameValid;
    }

    public String getVpeDefaultMaterialName () {
        return vpeDefaultMaterialName;
    }

    public void setVpeDefaultMaterialName (String vpeDefaultMaterialName) {
        this.vpeDefaultMaterialName = vpeDefaultMaterialName;
    }

    public String getStockMode () {
        return stockMode;
    }

    public void setStockMode (String stockMode) {
        this.stockMode = stockMode;
    }

    public String getUserUtilizationOverride () {
        return userUtilizationOverride;
    }

    public void setUserUtilizationOverride (String userUtilizationOverride) {
        this.userUtilizationOverride = userUtilizationOverride;
    }

    public String getUserMaterialName () {
        return userMaterialName;
    }

    public void setUserMaterialName (String userMaterialName) {
        this.userMaterialName = userMaterialName;
    }
}
