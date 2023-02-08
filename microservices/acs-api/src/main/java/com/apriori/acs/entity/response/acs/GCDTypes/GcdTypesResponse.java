package com.apriori.acs.entity.response.acs.GCDTypes;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GCDTypes.json")

public class GcdTypesResponse {
    private List<GcdListItemWithUnit> straightBend;
    private List<GcdListItemWithUnit> blank;
    private List<GcdListItemWithUnit> perimeter;
    private List<GcdListItemWithUnit> complexHole;
    private List<GcdListItemWithUnit> partingLine;
    private List<GcdListItemWithUnit> form;
    private List<GcdListItemWithUnit> lance;
    private List<GcdListItemWithUnit> shearForm;
    private List<GcdListItemWithUnit> planarFace;
    private List<GcdListItemWithUnit> simpleHole;
    private List<GcdListItemWithUnit> bulkRemoval;
    private List<GcdListItemWithUnit> multiBend;
    private List<GcdListItemWithUnit> multiStepHole;
    private List<GcdListItemWithUnit> notSupported;
    private List<GcdListItemWithUnit> axiGroove;
    private List<GcdListItemWithUnit> finishedPart;
    private List<GcdListItemWithUnit> curvedWall;
    @JsonProperty("@void")
    private List<GcdListItemWithUnit> voids;
    private List<GcdListItemWithUnit> curvedSurface;
    private List<GcdListItemWithUnit> component;
    private List<GcdListItemWithUnit> edge;
    private List<GcdListItemWithUnit> stockTrim;
    private List<GcdListItemWithUnit> displayName;
    private List<GcdListItemWithUnit> name;
    private List<GcdListItemWithUnit> storageType;
    private List<GcdListItemWithUnit> editable;
    private List<GcdListItemWithUnit> unitType;
}