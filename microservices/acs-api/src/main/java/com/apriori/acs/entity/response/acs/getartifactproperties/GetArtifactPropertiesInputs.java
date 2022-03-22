package com.apriori.acs.entity.response.acs.getartifactproperties;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetArtifactPropertiesInputs {
    private List<String> displayNames;
}
