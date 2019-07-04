package main.java.http.enums.common;

public enum TemporaryAPIEnum implements TemporaryAPI {

    GET_ACCOUNTS_STATUS("accounts/status")
    ;

    private final String endpoint;

    TemporaryAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

}
