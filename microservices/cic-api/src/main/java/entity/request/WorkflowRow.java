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
    public String key;
    public String value;
    public String identifier;
    public boolean isValid;
    public String mappingRule;
    public String twxAttributeName;
    public boolean _isSelected;
    public String dataType;
    public String displayDataType;
    public boolean isMandatoryField;
    public boolean isSaved;
    public boolean isStandardField;
    public String plmAttributeName;
    public boolean readOnly;
    public String usageType;
    public String errorMessage;
}
