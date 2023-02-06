package entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import entity.request.FieldDefinitionKey;
import entity.request.FieldDefinitionValue;
import lombok.Data;

@Data
public class AgentConnectionFieldDefinitions {
    private FieldDefinitionKey appKey;
    private FieldDefinitionValue connectionInfo;
}
