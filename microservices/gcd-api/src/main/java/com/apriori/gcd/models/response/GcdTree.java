package com.apriori.gcd.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "GcdTreeSchema.json")
@JsonRootName("response")
@Data
public class GcdTree {
    private List<GcdsAdded> gcdsAdded;
    private List<GcdsRemoved> gcdsRemoved;
}
