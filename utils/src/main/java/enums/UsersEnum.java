package main.java.enums;

/**
 *
 * @author kpatel
 */

public enum UsersEnum {

    CIE_TE_USER("cfrith@apriori.com", "TestEvent2018"),
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
