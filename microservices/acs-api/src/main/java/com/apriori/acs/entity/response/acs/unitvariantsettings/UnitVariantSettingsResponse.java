package com.apriori.acs.entity.response.acs.unitvariantsettings;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "acs/UnitVariantSettingsResponse.json")
public class UnitVariantSettingsResponse {
    @JsonProperty("CGM")
    private UnitVariantSetting cgm;
    @JsonProperty("CGS")
    private UnitVariantSetting cgs;
    @JsonProperty("FPM")
    private UnitVariantSetting fpm;
    @JsonProperty("FPS")
    private UnitVariantSetting fps;
    @JsonProperty("IOM")
    private UnitVariantSetting iom;
    @JsonProperty("IOS")
    private UnitVariantSetting ios;
    @JsonProperty("IPM")
    private UnitVariantSetting ipm;
    @JsonProperty("IPS")
    private UnitVariantSetting ips;
    @JsonProperty("MGM")
    private UnitVariantSetting mgm;
    @JsonProperty("MGS")
    private UnitVariantSetting mgs;
    @JsonProperty("MKM")
    private UnitVariantSetting mkm;
    @JsonProperty("MKS")
    private UnitVariantSetting mks;
    @JsonProperty("MMGM")
    private UnitVariantSetting mmgm;
    @JsonProperty("MMGS")
    private UnitVariantSetting mmgs;
    @JsonProperty("MMKS")
    private UnitVariantSetting mmks;

    public ArrayList<UnitVariantSetting> getAllUnitVariantSetting() {
        ArrayList<UnitVariantSetting> unitVariantSettings = new ArrayList<>();
        unitVariantSettings.add(getCgm());
        unitVariantSettings.add(getCgs());
        unitVariantSettings.add(getFpm());
        unitVariantSettings.add(getFps());
        unitVariantSettings.add(getIom());
        unitVariantSettings.add(getIos());
        unitVariantSettings.add(getIpm());
        unitVariantSettings.add(getIps());
        unitVariantSettings.add(getMgm());
        unitVariantSettings.add(getMgs());
        unitVariantSettings.add(getMkm());
        unitVariantSettings.add(getMks());
        unitVariantSettings.add(getMmgm());
        unitVariantSettings.add(getMmgs());
        unitVariantSettings.add(getMmks());
        return unitVariantSettings;
    }
}
