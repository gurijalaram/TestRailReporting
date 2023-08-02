package com.apriori.acs.models.response.acs.GcdTypes;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GcdTypesSheetMetalHydroforming.json")
public class GcdTypesSheetMetalHydroformingResponse {
    @JsonProperty("StraightBend")
    private List<GcdListItemWithUnit> straightBend;
    @JsonProperty("Blank")
    private List<GcdListItemWithUnit> blank;
    @JsonProperty("ComplexHole")
    private List<GcdListItemWithUnit> complexHole;
    @JsonProperty("AxiGroove")
    private List<GcdListItemWithUnit> axiGroove;
    @JsonProperty("Form")
    private List<GcdListItemWithUnit> form;
    @JsonProperty("EdgeSegment")
    private List<GcdListItemWithUnit> edgeSegment;
    @JsonProperty("CurvedWall")
    private List<GcdListItemWithUnit> curvedWall;
    @JsonProperty("PlanarFace")
    private List<GcdListItemWithUnit> planarFace;
    @JsonProperty("SimpleHole")
    private List<GcdListItemWithUnit> simpleHole;
    @JsonProperty("MultiBend")
    private List<GcdListItemWithUnit> multiBend;
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
