package entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldDefinitions {
    public FieldDefinitionKey key;
    public FieldDefinitionValue value;
    public FieldDefinitionKey dataType;
    public FieldDefinitionKey displayDataType;
    public FieldDefinitionKey identifier;
    public FieldDefinitionKey isMandatoryField;
    public FieldDefinitionKey isSaved;
    public FieldDefinitionKey isStandardField;
    public FieldDefinitionKey isValid;
    public FieldDefinitionKey mappingRule;
    public FieldDefinitionKey plmAttributeName;
    public FieldDefinitionKey readOnly;
    public FieldDefinitionKey twxAttributeName;
    public FieldDefinitionKey usageType;
    public FieldDefinitionKey _isSelected;
    public FieldDefinitionKey writingRule;
    public FieldDefinitionKey isDefault;
}
