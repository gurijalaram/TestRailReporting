package com.apriori.acs.entity.response.acs.GcdProperties;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertiesToSet {
    private String roughnessRz;
    private String roughness;
    private String tolerance;
}
