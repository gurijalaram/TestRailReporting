package main.java.http.enums;


/**
 * @author kpatel
 */
public interface EndpointEnum {

    String getEndpoint(Object... variables);

    String getEndpointString();
}
