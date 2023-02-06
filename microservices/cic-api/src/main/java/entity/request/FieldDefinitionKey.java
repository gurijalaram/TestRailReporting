package entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldDefinitionKey {
    private String name;
    private String description;
    private String baseType;
    private Integer ordinal;
    private Aspects aspects;
}
