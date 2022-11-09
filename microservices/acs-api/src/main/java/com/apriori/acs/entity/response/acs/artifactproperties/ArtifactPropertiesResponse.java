package com.apriori.acs.entity.response.acs.artifactproperties;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/ArtifactPropertiesResponse.json")
public class ArtifactPropertiesResponse {
    private List<ArtifactListItem> artifactList;
    private TypedMapBean typedMapBean;
}