package com.apriori.acs.entity.response.acs.GcdTypes;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GcdTypesSheetMetalRollForming.json")
public class GcdTypesSheetMetalRollFormingResponse {
    @JsonProperty("StraightBend")
    private List<GcdListItemWithUnit> straightBend;
    @JsonProperty("Blank")
    private List<GcdListItemWithUnit> blank;
    @JsonProperty("ComplexHole")
    private List<GcdListItemWithUnit> complexHole;
    @JsonProperty("Form")
    private List<GcdListItemWithUnit> form;
    @JsonProperty("Bend")
    private List<GcdListItemWithUnit> bend;
    @JsonProperty("Lance")
    private List<GcdListItemWithUnit> lance;
    @JsonProperty("PlanarFace")
    private List<GcdListItemWithUnit> planarFace;
    @JsonProperty("SimpleHole")
    private List<GcdListItemWithUnit> simpleHole;
    @JsonProperty("AxiGroove")
    private List<GcdListItemWithUnit> axiGroove;
    @JsonProperty("Cutout")
    private List<GcdListItemWithUnit> cutout;
    @JsonProperty("CurvedWall")
    private List<GcdListItemWithUnit> curvedWall;
    @JsonProperty("End")
    private List<GcdListItemWithUnit> end;
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
