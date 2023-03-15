package com.apriori.ach.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserPreferenceInFailureResponse {
    private String createdBy;
    private String name;
    private String type;
    private Integer value;
}