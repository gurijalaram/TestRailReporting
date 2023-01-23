package entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailConfiguration {
    public DataShape dataShape;
    public List<WorkflowRow> rows;
}
