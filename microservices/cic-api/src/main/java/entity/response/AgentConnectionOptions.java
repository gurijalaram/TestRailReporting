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
    private String appKey;
    private String wssUrl;
    private String agentName;
    private String installDirectory;
    private String port;
    private String authToken;
    private String plmType;
    private Integer scanRate;
    private Integer reconnectionInterval;
}
