package com.apriori.cidappapi.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CustomAttributes {
    private Integer workspaceId;
}
