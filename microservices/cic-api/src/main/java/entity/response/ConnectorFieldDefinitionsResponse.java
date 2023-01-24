package entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import entity.request.FieldDefinitionKey;
import lombok.Data;

@Data
public class ConnectorFieldDefinitionsResponse {
    public FieldDefinitionKey connectionStatus;
    public FieldDefinitionKey customerId;
    public FieldDefinitionKey description;
    public FieldDefinitionKey displayName;
    public FieldDefinitionKey displayNameNoBreakSpaces;
    public FieldDefinitionKey hasRelatedWorkflows;
    public FieldDefinitionKey id;
    @JsonProperty("isPlmConnected")
    public FieldDefinitionKey plmConnected;
    public FieldDefinitionKey name;
    public FieldDefinitionKey type;
    public FieldDefinitionKey typeDisplayName;
    public FieldDefinitionKey useLocalConfiguration;
}
