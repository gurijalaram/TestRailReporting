package com.apriori.acs.api.models.response.acs.GcdTypes;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GcdTypes.json")
public class GcdTypesResponse {
    @JsonProperty("StraightBend")
    private List<GcdListItemWithUnit> straightBend;
    @JsonProperty("Blank")
    private List<GcdListItemWithUnit> blank;
    @JsonProperty("Perimeter")
    private List<GcdListItemWithUnit> perimeter;
    @JsonProperty("ComplexHole")
    private List<GcdListItemWithUnit> complexHole;
    @JsonProperty("PartingLine")
    private List<GcdListItemWithUnit> partingLine;
    @JsonProperty("Form")
    private List<GcdListItemWithUnit> form;
    @JsonProperty("Lance")
    private List<GcdListItemWithUnit> lance;
    @JsonProperty("ShearForm")
    private List<GcdListItemWithUnit> shearForm;
    @JsonProperty("PlanarFace")
    private List<GcdListItemWithUnit> planarFace;
    @JsonProperty("SimpleHole")
    private List<GcdListItemWithUnit> simpleHole;
    @JsonProperty("BulkRemoval")
    private List<GcdListItemWithUnit> bulkRemoval;
    @JsonProperty("MultiBend")
    private List<GcdListItemWithUnit> multiBend;
    @JsonProperty("MultiStepHole")
    private List<GcdListItemWithUnit> multiStepHole;
    @JsonProperty("NotSupported")
    private List<GcdListItemWithUnit> notSupported;
    @JsonProperty("AxiGroove")
    private List<GcdListItemWithUnit> axiGroove;
    @JsonProperty("FinishedPart")
    private List<GcdListItemWithUnit> finishedPart;
    @JsonProperty("CurvedWall")
    private List<GcdListItemWithUnit> curvedWall;
    @JsonProperty("Void")
    private List<GcdListItemWithUnit> voids;
    @JsonProperty("CurvedSurface")
    private List<GcdListItemWithUnit> curvedSurface;
    @JsonProperty("Component")
    private List<GcdListItemWithUnit> component;
    @JsonProperty("Edge")
    private List<GcdListItemWithUnit> edge;
    @JsonProperty("StockTrim")
    private List<GcdListItemWithUnit> stockTrim;
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