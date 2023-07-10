package entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowRow {
    private String key;
    private String value;
    private String identifier;
    private String mappingRule;
    private String twxAttributeName;
    private Boolean _isSelected;
    private String dataType;
    private String displayDataType;
    private Boolean isMandatoryField;
    private Boolean isSaved;
    private Boolean isValid;
    private Boolean isStandardField;
    private String plmAttributeName;
    private Boolean readOnly;
    private String usageType;
    private String errorMessage;
    private String writingRule;
}
