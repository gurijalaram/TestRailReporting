package entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentConnectionOptions {
    //General
    private String installDirectory;
    private Integer port;
    private String authTokenHeaderName;
    private String authToken;
    //CostInsightConnectProperties
    private String wssUrl;
    private String appKey;
    private Integer scanRate;
    private Integer reconnectionInterval;
    private Integer invokeServiceTimeout;
    private String agentName;
    private String agentId;
    private String plmType;
    private String websocketProxyHost;
    private Integer websocketProxyPort;
    private String websocketProxyUser;
    private String websocketProxyPassword;
    //[PlmProperties]
    private String hostName;
    private String plmUser;
    private String plmPassword;
    private String fscUrl;
    private String rootFolderPath;
    private Integer csrfTokenTimeoutSeconds;
    private String maxPartsToReturn;
    private String itemTypeRevisionsItemRevision;
    private String authenticationProtocol;
    private String enoviaVersion;
    private String libSource;
    //[OAuthProperties]
    private String oAuthBaseUrl;
    private String clientId;
    private String clientSecret;
    private String tokenScopes;
    private String tokenAudience;
}
