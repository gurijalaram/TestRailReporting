package com.apriori.acs.entity.response.getunitvariantsettings;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "GetUnitVariantSettingsResponse.json")
public class GetUnitVariantSettingsResponse {
    @JsonProperty("CGM")
    private UnitVariantSetting CGM;
    @JsonProperty("CGS")
    private UnitVariantSetting CGS;
    @JsonProperty("FPM")
    private UnitVariantSetting FPM;
    @JsonProperty("FPS")
    private UnitVariantSetting FPS;
    @JsonProperty("IOM")
    private UnitVariantSetting IOM;
    @JsonProperty("IOS")
    private UnitVariantSetting IOS;
    @JsonProperty("IPM")
    private UnitVariantSetting IPM;
    @JsonProperty("IPS")
    private UnitVariantSetting IPS;
    @JsonProperty("MGM")
    private UnitVariantSetting MGM;
    @JsonProperty("MGS")
    private UnitVariantSetting MGS;
    @JsonProperty("MKM")
    private UnitVariantSetting MKM;
    @JsonProperty("MKS")
    private UnitVariantSetting MKS;
    @JsonProperty("MMGM")
    private UnitVariantSetting MMGM;
    @JsonProperty("MMGS")
    private UnitVariantSetting MMGS;
    @JsonProperty("MMKS")
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
