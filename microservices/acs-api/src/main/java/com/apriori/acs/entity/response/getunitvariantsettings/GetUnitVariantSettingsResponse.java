package com.apriori.acs.entity.response.getunitvariantsettings;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "GetUnitVariantSettingsResponse.json")
public class GetUnitVariantSettingsResponse {
    @JsonProperty
    private UnitVariantSetting CGM;
    @JsonProperty
    private UnitVariantSetting CGS;
    @JsonProperty
    private UnitVariantSetting FPM;
    @JsonProperty
    private UnitVariantSetting FPS;
    @JsonProperty
    private UnitVariantSetting IOM;
    @JsonProperty
    private UnitVariantSetting IOS;
    @JsonProperty
    private UnitVariantSetting IPM;
    @JsonProperty
    private UnitVariantSetting IPS;
    @JsonProperty
    private UnitVariantSetting MGM;
    @JsonProperty
    private UnitVariantSetting MGS;
    @JsonProperty
    private UnitVariantSetting MKM;
    @JsonProperty
    private UnitVariantSetting MKS;
    @JsonProperty
    private UnitVariantSetting MMGM;
    @JsonProperty
    private UnitVariantSetting MMGS;
    @JsonProperty
    private UnitVariantSetting MMKS;

    public ArrayList<UnitVariantSetting> getAllUnitVariantSetting() {
        ArrayList<UnitVariantSetting> unitVariantSettings = new ArrayList<>();
        unitVariantSettings.add(getCGM());
        unitVariantSettings.add(getCGS());
        unitVariantSettings.add(getFPM());
        unitVariantSettings.add(getFPS());
        unitVariantSettings.add(getIOM());
        unitVariantSettings.add(getIOS());
        unitVariantSettings.add(getIPM());
        unitVariantSettings.add(getIPS());
        unitVariantSettings.add(getMGM());
        unitVariantSettings.add(getMGS());
        unitVariantSettings.add(getMKM());
        unitVariantSettings.add(getMKS());
        unitVariantSettings.add(getMMGM());
        unitVariantSettings.add(getMMGS());
        unitVariantSettings.add(getMMKS());
        return unitVariantSettings;
    }
}
