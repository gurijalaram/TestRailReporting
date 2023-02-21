package com.apriori.acs.entity.response.acs.GcdProperties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertiesToSet {
    private String roughnessRz;
    private String roughness;
    private String tolerance;
}
