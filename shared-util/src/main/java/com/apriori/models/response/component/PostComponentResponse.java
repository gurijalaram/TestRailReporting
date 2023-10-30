package com.apriori.models.response.component;

import com.apriori.annotations.Schema;
import com.apriori.models.request.component.Failures;
import com.apriori.models.request.component.Successes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CreateComponentResponse.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonRootName("response")
public class PostComponentResponse {
    private List<Successes> successes;
    private List<Failures> failures;
}

