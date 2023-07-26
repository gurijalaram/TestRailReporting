package com.apriori.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ValidateSiteSchema.json")
@Data
@JsonRootName("response")
public class ValidateSite {
    private String status;
}