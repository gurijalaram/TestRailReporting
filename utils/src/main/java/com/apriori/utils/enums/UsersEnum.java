package com.apriori.utils.enums;

/**
 * @author kpatel
 */
@Deprecated
public enum UsersEnum {

    CID_TE_USER_ALLDATA("qa-automation-12@apriori.com", "qa-automation-12"),
    CID_TE_USER("qa-automation-18@apriori.com", "TrumpetSnakeFridgeToasty18"),
    ADMIN_DEFAULT_USER("admin@apriori.com", "admin");

    private final String username;
    private final String password;

    UsersEnum(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // The following qa-auto users have restricted access:
    // qa-automation-37@apriori.com does not have read access to UDA project name “Eagle”
    // qa-automation-38@apriori.com cannot read parts with VPE China
    // qa-automation-39@apriori.com cannot update parts with PG Sheet Metal or delete any parts with VPE Brazil
    // qa-automation-40@apriori.com cannot cost using VPE aPriori USA
    // qa-automation password TrumpetSnakeFridgeToasty18

    /**
     * Return username of the requested user
     *
     * @return CID username
     */
    @Deprecated
    public String getUsername() {
        return username;
    }

    /**
     * Return password of the requested user
     *
     * @return cid password
     */
    @Deprecated
    public String getPassword() {
        return password;
    }

}
