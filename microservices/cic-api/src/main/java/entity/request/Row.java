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
public class Row {
    public String key;
    public String value;
    public String identifier;
    public boolean isValid;
    public String mappingRule;
    public String twxAttributeName;
    public boolean _isSelected;
}
