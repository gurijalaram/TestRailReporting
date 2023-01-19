package entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectorInfo {
    private String connectionStatus;
    private String customerId;
    private String description;
    private String displayName;
    private boolean hasRelatedWorkflows;
    private String id;
    @JsonProperty("isPlmConnected")
    private boolean plmConnected;
    private String name;
    private String type;
    private String typeDisplayName;
    private boolean useLocalConfiguration;
}
