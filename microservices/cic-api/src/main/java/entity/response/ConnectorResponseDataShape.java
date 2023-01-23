package entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import entity.request.FieldDefinitions;
import lombok.Data;

@Data
public class ConnectorResponseDataShape {
    public ConnectorFieldDefinitionsResponse fieldDefinitions;
}
