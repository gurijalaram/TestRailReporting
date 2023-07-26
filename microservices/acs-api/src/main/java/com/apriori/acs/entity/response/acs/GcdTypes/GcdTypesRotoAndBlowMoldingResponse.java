package com.apriori.acs.entity.response.acs.GcdTypes;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GcdTypesRotoAndBlowMolding.json")
public class GcdTypesRotoAndBlowMoldingResponse {
    @JsonProperty("ComboVoid")
    private List<GcdListItemWithUnit> comboVoid;
    @JsonProperty("Perimeter")
    private List<GcdListItemWithUnit> perimeter;
    @JsonProperty("ComplexHole")
    private List<GcdListItemWithUnit> complexHole;
    @JsonProperty("AxiGroove")
    private List<GcdListItemWithUnit> axiGroove;
    @JsonProperty("PartingLine")
    private List<GcdListItemWithUnit> partingLine;
    @JsonProperty("Spout")
    private List<GcdListItemWithUnit> spout;
    @JsonProperty("CurvedWall")
    private List<GcdListItemWithUnit> curvedWall;
    @JsonProperty("PlanarFace")
    private List<GcdListItemWithUnit> planarFace;
    @JsonProperty("SimpleHole")
    private List<GcdListItemWithUnit> simpleHole;
    @JsonProperty("Void")
    private List<GcdListItemWithUnit> voids;
    @JsonProperty("CurvedSurface")
    private List<GcdListItemWithUnit> curvedSurface;
    @JsonProperty("Component")
    private List<GcdListItemWithUnit> component;
    @JsonProperty("MultiStepHole")
    private List<GcdListItemWithUnit> multiStepHole;
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
