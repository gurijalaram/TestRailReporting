package entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldDefinitionValue {
    public String name;
    public String description;
    public String baseType;
    public Integer ordinal;
    public Aspects aspects;
}
