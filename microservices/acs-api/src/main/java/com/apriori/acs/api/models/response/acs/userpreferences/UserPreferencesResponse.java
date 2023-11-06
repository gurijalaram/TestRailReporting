package com.apriori.acs.api.models.response.acs.userpreferences;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@Schema(location = "acs/UserPreferencesResponse.json")
public class UserPreferencesResponse {
    @JsonProperty("apriori.free.bodies.ignore.missing.component")
    private String aprioriFreeBodiesIgnoreMissingComponent;
    @JsonProperty("apriori.free.bodies.preserve.CAD")
    private String aprioriFreeBodiesPreserveCAD;
    @JsonProperty("apriori.keep.free.bodies")
    private String aprioriKeepFreeBodies;
    @JsonProperty("componentsTable.scenarioKey.width")
    private String componentsTableScenarioKeyWidth;
    @JsonProperty("cost.table.decimal.places")
    private String costTableDecimalPlaces;
    private String defaultScenarioName;
    @JsonProperty("prod.info.default.annual.volume")
    private String prodInfoDefaultAnnualVolume;
    @JsonProperty("prod.info.default.batch.size")
    private String prodInfoDefaultBatchSize;
    @JsonProperty("prod.info.default.material.catalog.name")
    private String prodInfoDefaultMaterialCatalogName;
    @JsonProperty("prod.info.default.material")
    private String prodInfoDefaultMaterial;
    @JsonProperty("prod.info.default.pg")
    private String prodInfoDefaultPg;
    @JsonProperty("prod.info.default.production.life")
    private String prodInfoDefaultProductionLife;
    @JsonProperty("prod.info.default.use.vpe.for.all.processes")
    private String prodInfoDefaultUseVpeForAllProcesses;
    @JsonProperty("prod.info.vpe")
    private String prodInfoVpe;
    private String selectionColor;
    @JsonProperty("show.component.list.preview.panel")
    private String showComponentListPreviewPanel;
    @JsonProperty("TolerancePolicyDefaults.bendAngleToleranceOverride")
    private String tolerancePolicyDefaultsBendAngleToleranceOverride;
    @JsonProperty("TolerancePolicyDefaults.cadToleranceReplacement")
    private String tolerancePolicyDefaultsCadToleranceReplacement;
    @JsonProperty("TolerancePolicyDefaults.circularityOverride")
    private String circularityOverride;
    @JsonProperty("TolerancePolicyDefaults.concentricityOverride")
    private String concentricityOverride;
    @JsonProperty("TolerancePolicyDefaults.cylindricityOverride")
    private String cylindricityOverride;
    @JsonProperty("TolerancePolicyDefaults.diamToleranceOverride")
    private String tolerancePolicyDefaultsDiamToleranceOverride;
    @JsonProperty("TolerancePolicyDefaults.flatnessOverride")
    private String tolerancePolicyDefaultsFlatnessOverride;
    @JsonProperty("TolerancePolicyDefaults.minCadToleranceThreshhold")
    private String tolerancePolicyDefaultsMinCadToleranceThreshhold;
    @JsonProperty("TolerancePolicyDefaults.parallelismOverride")
    private String tolerancePolicyDefaultsParallelismOverride;
    @JsonProperty("TolerancePolicyDefaults.perpendicularityOverride")
    private String tolerancePolicyDefaultsPerpendicularityOverride;
    @JsonProperty("TolerancePolicyDefaults.positionToleranceOverride")
    private String tolerancePolicyDefaultsPositionToleranceOverride;
    @JsonProperty("TolerancePolicyDefaults.profileOfSurfaceOverride")
    private String tolerancePolicyDefaultsProfileOfSurfaceOverride;
    @JsonProperty("TolerancePolicyDefaults.roughnessOverride")
    private String tolerancePolicyDefaultsRoughnessOverride;
    @JsonProperty("TolerancePolicyDefaults.roughnessRzOverride")
    private String tolerancePolicyDefaultsRoughnessRzOverride;
    @JsonProperty("TolerancePolicyDefaults.runoutOverride")
    private String tolerancePolicyDefaultsRunoutOverride;
    @JsonProperty("TolerancePolicyDefaults.straightnessOverride")
    private String tolerancePolicyDefaultsStraightnessOverride;
    @JsonProperty("TolerancePolicyDefaults.symmetryOverride")
    private String tolerancePolicyDefaultsSymmetryOverride;
    @JsonProperty("TolerancePolicyDefaults.toleranceMode")
    private String tolerancePolicyDefaultsToleranceMode;
    @JsonProperty("TolerancePolicyDefaults.toleranceOverride")
    private String tolerancePolicyDefaultsToleranceOverride;
    @JsonProperty("TolerancePolicyDefaults.totalRunoutOverride")
    private String tolerancePolicyDefaultsTotalRunoutOverride;
    @JsonProperty("TolerancePolicyDefaults.useCadToleranceThreshhold")
    private String tolerancePolicyDefaultsUseCadToleranceThreshhold;
    @JsonProperty("web.login")
    private String webLogin;
    @JsonProperty("workspace.filter.private.sort.column")
    private String workspaceFilterPrivateSortColumn;
    @JsonProperty("workspace.filter.private.sort.direction")
    private String workspaceFilterPrivateSortDirection;
    @JsonProperty("workspace.filter.public.sort.column")
    private String workspaceFilterPublicSortColumn;
    @JsonProperty("workspace.filter.public.sort.direction")
    private String workspaceFilterPublicSortDirection;
}
