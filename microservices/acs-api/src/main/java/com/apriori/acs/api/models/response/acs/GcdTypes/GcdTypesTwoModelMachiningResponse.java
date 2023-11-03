package com.apriori.acs.api.models.response.acs.GcdTypes;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GcdTypesTwoModelMachining.json")
public class GcdTypesTwoModelMachiningResponse {
    @JsonProperty("SourceModel")
    private List<GcdListItemWithUnit> sourceModel;
    @JsonProperty("AxiGroove")
    private List<GcdListItemWithUnit> axiGroove;
    @JsonProperty("Ring")
    private List<GcdListItemWithUnit> ring;
    @JsonProperty("CurvedWall")
    private List<GcdListItemWithUnit> curvedWall;
    @JsonProperty("PlanarFace")
    private List<GcdListItemWithUnit> planarFace;
    @JsonProperty("CurvedSurface")
    private List<GcdListItemWithUnit> curvedSurface;
    @JsonProperty("Component")
    private List<GcdListItemWithUnit> component;
    @JsonProperty("DisplayName")
    private List<GcdListItemWithUnit> displayName;
    @JsonProperty("Name")
    private List<GcdListItemWithUnit> name;
    @JsonProperty("StorageType")
    private List<GcdListItemWithUnit> storageType;
    @JsonProperty("Editable")
    private List<GcdListItemWithUnit> editable;
    @JsonProperty("UnitType")
    private List<GcdListItemWithUnit> unitType;
}