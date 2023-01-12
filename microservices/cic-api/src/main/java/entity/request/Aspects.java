package entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Aspects {
    public Boolean isPrimaryKey;
    public Boolean defaultValue;
}
