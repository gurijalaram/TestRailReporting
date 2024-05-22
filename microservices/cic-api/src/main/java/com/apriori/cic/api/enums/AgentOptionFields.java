package com.apriori.cic.api.enums;

public enum AgentOptionFields {
    INSTALL_DIRECTORY("installDirectory="),
    AUTH_TOKEN_HEADER_NAME("auth-token-header-name="),
    AGENT_PORT("port="),
    AUTH_TOKEN("auth-token="),
    WSS_URL("url="),
    APP_KEY("appKey="),
    SCAN_RATE("scanRate="),
    RECONNECTION_INTERVAL("reconnectionInterval="),
    INVOKE_SERVICE_TIMEOUT("invokeServiceTimeout="),
    AGENT_ID("agentId="),
    PLM_TYPE("plmType="),
    WEB_SOCKET_PROXY_HOST("websocketProxyHost="),
    WEB_SOCKET_PROXY_PORT("websocketProxyPort="),
    WEB_SOCKET_PROXY_USER("websocketProxyUser="),
    WEB_SOCKET_PROXY_PASSWORD("websocketProxyPassword="),
    HOST_NAME("hostName="),
    PLM_USER("user="),
    PLM_PASSWORD("password="),
    FSC_URL("fscUrl="),
    ROOT_FOLDER_PATH("rootFolderPath="),
    CSRF_TOKEN_TIMEOUT_SECONDS("csrfTokenTimeoutSeconds="),
    MAX_PARTS_TO_RETURN("maxPartsToReturn="),
    ITEM_TYPE_REVISIONS("itemTypeRevisions="),
    AUTHENTICATION_PROTOCOL("authenticationProtocol="),
    ENOVIA_VERSION("enoviaVersion="),
    LIB_SOURCE("libSource="),
    OAUTH_BASE_URL("oAuthBaseUrl="),
    CLIENT_ID("clientId="),
    CLIENT_SECRET("clientSecret="),
    TOKEN_SCOPES("tokenScopes="),
    TOKEN_AUDIENCE("tokenAudience=");

    private final String agentOptionField;

    AgentOptionFields(String type) {
        agentOptionField = type;
    }

    public String getAgentOptionField() {
        return agentOptionField;
    }
}
