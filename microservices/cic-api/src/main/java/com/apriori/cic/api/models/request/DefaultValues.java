package com.apriori.cic.api.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultValues {
    public DataShape dataShape;
    public List<WorkflowRow> rows;
    public String name;
    public String description;
}
