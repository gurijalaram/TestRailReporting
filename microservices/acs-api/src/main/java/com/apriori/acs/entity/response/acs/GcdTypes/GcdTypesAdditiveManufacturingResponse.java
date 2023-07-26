package com.apriori.acs.entity.response.acs.GcdTypes;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GcdTypesAdditiveManufacturing.json")
public class GcdTypesAdditiveManufacturingResponse {
    @JsonProperty("Perimeter")
    private List<GcdListItemWithUnit> perimeter;
    @JsonProperty("SupportStructure")
    private List<GcdListItemWithUnit> supportStructure;
    @JsonProperty("PlanarFace")
    private List<GcdListItemWithUnit> planarFace;
    @JsonProperty("SimpleHole")
    private List<GcdListItemWithUnit> simpleHole;
    @JsonProperty("MultiStepHole")
    private List<GcdListItemWithUnit> multiStepHole;
    @JsonProperty("AxiGroove")
    private List<GcdListItemWithUnit> axiGroove;
    @JsonProperty("Keyway")
    private List<GcdListItemWithUnit> keyway;
    @JsonProperty("Ring")
    private List<GcdListItemWithUnit> ring;
    @JsonProperty("CurvedWall")
    private List<GcdListItemWithUnit> curvedWall;
    @JsonProperty("RingedHole")
    private List<GcdListItemWithUnit> ringedHole;
    @JsonProperty("BuildDirection")
    private List<GcdListItemWithUnit> buildDirection;
    @JsonProperty("Void")
    private List<GcdListItemWithUnit> voids;
    @JsonProperty("CurvedSurface")
    private List<GcdListItemWithUnit> curvedSurface;
    @JsonProperty("Component")
    private List<GcdListItemWithUnit> component;
    @JsonProperty("Edge")
    private List<GcdListItemWithUnit> edge;
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
