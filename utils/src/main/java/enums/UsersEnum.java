package main.java.enums;

/**
 *
 * @author kpatel
 */

public enum UsersEnum {

    CIE_TE_USER("cfrith@apriori.com", "TestEvent2018");

    private final String username;
    private final String password;

    UsersEnum(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Return username of the requested user
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return password of the requested user
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

}
