package com.apriori.cds.api.enums;

import java.util.Arrays;
import java.util.List;

public enum CDSRolesEnum {
    CONTRIBUTOR("APRIORI_CONTRIBUTOR", Arrays.asList(AppAccessControlsEnum.CUS, AppAccessControlsEnum.CSS,
        AppAccessControlsEnum.ACH, AppAccessControlsEnum.AW)),
    ANALYST("APRIORI_ANALYST", Arrays.asList(AppAccessControlsEnum.CUS, AppAccessControlsEnum.CSS,
        AppAccessControlsEnum.ACH, AppAccessControlsEnum.AW, AppAccessControlsEnum.AA)),
    DESIGNER("APRIORI_DESIGNER", Arrays.asList(AppAccessControlsEnum.CUS, AppAccessControlsEnum.CSS,
        AppAccessControlsEnum.ACH, AppAccessControlsEnum.AW, AppAccessControlsEnum.AA, AppAccessControlsEnum.AD,
        AppAccessControlsEnum.FMS, AppAccessControlsEnum.ACS)),
    DEVELOPER("APRIORI_DEVELOPER", Arrays.asList(AppAccessControlsEnum.CUS, AppAccessControlsEnum.CSS,
        AppAccessControlsEnum.ACH, AppAccessControlsEnum.AW, AppAccessControlsEnum.AA, AppAccessControlsEnum.AD,
        AppAccessControlsEnum.FMS, AppAccessControlsEnum.ACS, AppAccessControlsEnum.EDC, AppAccessControlsEnum.AP)),
    EXPERT("APRIORI_EXPERT", Arrays.asList(AppAccessControlsEnum.CUS, AppAccessControlsEnum.CSS,
        AppAccessControlsEnum.ACH, AppAccessControlsEnum.AW, AppAccessControlsEnum.AA, AppAccessControlsEnum.AD,
        AppAccessControlsEnum.FMS, AppAccessControlsEnum.ACS, AppAccessControlsEnum.EDC, AppAccessControlsEnum.AP)),
    ANALYST_CONNECT("APRIORI_ANALYST + CONNECT ADMIN", Arrays.asList(AppAccessControlsEnum.CUS, AppAccessControlsEnum.CSS,
        AppAccessControlsEnum.ACH, AppAccessControlsEnum.AW, AppAccessControlsEnum.AA, AppAccessControlsEnum.AC)),
    DESIGNER_CONNECT_USER("APRIORI_DESIGNER + CONNECT ADMIN + USER ADMIN", Arrays.asList(AppAccessControlsEnum.CUS, AppAccessControlsEnum.CSS,
        AppAccessControlsEnum.ACH, AppAccessControlsEnum.AW, AppAccessControlsEnum.AA, AppAccessControlsEnum.AD,
        AppAccessControlsEnum.FMS, AppAccessControlsEnum.ACS, AppAccessControlsEnum.AC, AppAccessControlsEnum.CA)),
    EXPERT_CONNECT_USER("APRIORI_EXPERT + CONNECT ADMIN + USER ADMIN", Arrays.asList(AppAccessControlsEnum.CUS, AppAccessControlsEnum.CSS,
        AppAccessControlsEnum.ACH, AppAccessControlsEnum.AW, AppAccessControlsEnum.AA, AppAccessControlsEnum.AD,
        AppAccessControlsEnum.FMS, AppAccessControlsEnum.ACS, AppAccessControlsEnum.EDC, AppAccessControlsEnum.AP,
        AppAccessControlsEnum.AC, AppAccessControlsEnum.CA)),
    EXPERT_CONNECT_USER_SANDBOX("APRIORI_EXPERT + CONNECT ADMIN + USER ADMIN + SANDBOX", Arrays.asList(AppAccessControlsEnum.AW, AppAccessControlsEnum.AA,
        AppAccessControlsEnum.AD, AppAccessControlsEnum.AP, AppAccessControlsEnum.ADMIN, AppAccessControlsEnum.AC, AppAccessControlsEnum.FMS,
        AppAccessControlsEnum.ACS, AppAccessControlsEnum.CSS)),
    EXPERT_CONNECT_USER_ADMIN("APRIORI_EXPERT + CONNECT ADMIN + USER ADMIN + EXPORT ADMIN", Arrays.asList(AppAccessControlsEnum.CUS, AppAccessControlsEnum.CSS,
        AppAccessControlsEnum.ACH, AppAccessControlsEnum.AW, AppAccessControlsEnum.AA, AppAccessControlsEnum.AD,
        AppAccessControlsEnum.FMS, AppAccessControlsEnum.ACS, AppAccessControlsEnum.EDC, AppAccessControlsEnum.AP,
        AppAccessControlsEnum.AC, AppAccessControlsEnum.CA, AppAccessControlsEnum.ADMIN));

    private final String role;
    private final List<AppAccessControlsEnum> apps;

    CDSRolesEnum(String role, List<AppAccessControlsEnum> apps) {
        this.role = role;
        this.apps = apps;
    }

    public String getRole() {
        return role;
    }

    public List<AppAccessControlsEnum> getApps() {
        return apps;
    }
}
