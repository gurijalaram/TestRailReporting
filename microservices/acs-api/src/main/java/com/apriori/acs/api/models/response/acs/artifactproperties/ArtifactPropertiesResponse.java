package com.apriori.acs.api.models.response.acs.artifactproperties;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/ArtifactPropertiesResponse.json")
public class ArtifactPropertiesResponse {
    private List<ArtifactListItem> artifactList;
    private TypedMapBean typedMapBean;
}
