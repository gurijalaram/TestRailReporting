package com.apriori.acs.entity.response.acs.getartifactproperties;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GetArtifactPropertiesResponse.json")
public class GetArtifactPropertiesResponse {
    private List<ArtifactListItem> artifactList;
    private TypedMapBean typedMapBean;
}
