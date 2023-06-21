package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "RequiredPropertiesSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
public class RequiredProperties {
    private String name;
    private String source;
}