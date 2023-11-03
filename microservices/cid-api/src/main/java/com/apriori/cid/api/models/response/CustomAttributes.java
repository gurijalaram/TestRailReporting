package com.apriori.cid.api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CustomAttributes {
    //cn - as per https://jira.apriori.com/browse/BA-2458 this class should be left empty
}
