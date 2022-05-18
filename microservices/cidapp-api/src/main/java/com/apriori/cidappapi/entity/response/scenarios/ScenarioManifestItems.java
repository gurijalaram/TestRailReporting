package com.apriori.cidappapi.entity.response.scenarios;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Schema(location = "ScenarioManifestItems.json")
@Data
public class ScenarioManifestItems extends Pagination {
    private List<ScenarioManifest> subcomponents;
}
