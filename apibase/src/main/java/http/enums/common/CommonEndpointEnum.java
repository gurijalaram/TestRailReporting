package main.java.http.enums.common;

/**
 * @author kpatel
 */
public enum CommonEndpointEnum implements InternalEndpointEnum {

    POST_SESSIONID("/login"),
    GET_ACCOUNTS_STATUS("/accounts/status")

    ;

    private final String endpoint;

    CommonEndpointEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

}
