package entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import entity.request.FieldDefinitionKey;
import lombok.Data;

@Data
public class ReportTemplateDataShape {
    private ReportTemplateFields fieldDefinitions;
}
