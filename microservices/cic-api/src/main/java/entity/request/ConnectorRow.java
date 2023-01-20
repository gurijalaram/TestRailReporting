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
public class ConnectorRow {
    private String dataType;
    private String displayDataType;
    private String identifier;
    private boolean isMandatoryField;
    private boolean isSaved;
    private boolean isStandardField;
    private String plmAttributeName;
    private boolean readOnly;
    private String twxAttributeName;
    private String usageType;
    private boolean _isSelected;
    private String errorMessage;
    private boolean isValid;
}
