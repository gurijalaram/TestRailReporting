package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "common/ApVersionSchema.json")
@JsonRootName("response")
@Data
public class ApVersion {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String version;
}
