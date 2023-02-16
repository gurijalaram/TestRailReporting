package entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import entity.request.FieldDefinitionKey;
import lombok.Data;

@Data
public class ReportTemplateFields {
    public FieldDefinitionKey displayName;
    public FieldDefinitionKey isDisabled;
    public FieldDefinitionKey sectionName;
    public FieldDefinitionKey value;
}
