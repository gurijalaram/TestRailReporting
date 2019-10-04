package com.apriori.utils.enums;

/**
 * @author kpatel
 */

public enum UsersEnum {

    CID_TE_USER_ALLDATA("qa-automation-02@apriori.com", "qa-automation-02"),
    CID_TE_USER("qa-automation-02@apriori.com", "TrumpetSnakeFridgeToasty18"),
    ADMIN_DEFAULT_USER("admin@apriori.com", "admin");

    private final String username;
    private final String password;

    UsersEnum(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Return username of the requested user
     *
     * @return CID username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return password of the requested user
     *
     * @return cid password
     */
    public String getPassword() {
        return password;
    }

}
