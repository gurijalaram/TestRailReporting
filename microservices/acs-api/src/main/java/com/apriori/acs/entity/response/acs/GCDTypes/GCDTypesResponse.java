package com.apriori.acs.entity.response.acs.GCDTypes;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GCDTypes.json")

public class GCDTypesResponse {
    private List<GCDListItemwithUnit> straightBend;
    private List<GCDListItemwithUnit> blank;
    private List<GCDListItemwithUnit> perimeter;
    private List<GCDListItemwithUnit> complexHole;
    private List<GCDListItemwithUnit> partingLine;
    private List<GCDListItemwithUnit> form;
    private List<GCDListItemwithUnit> lance;
    private List<GCDListItemwithUnit> shearForm;
    private List<GCDListItemwithUnit> planarFace;
    private List<GCDListItemwithUnit> simpleHole;
    private List<GCDListItemwithUnit> bulkRemoval;
    private List<GCDListItemwithUnit> multiBend;
    private List<GCDListItemwithUnit> multiStepHole;
    private List<GCDListItemwithUnit> notSupported;
    private List<GCDListItemwithUnit> axiGroove;
    private List<GCDListItemwithUnit> finishedPart;
    private List<GCDListItemwithUnit> curvedWall;
    @JsonProperty("@void")
    private List<GCDListItemwithUnit> voids;
    private List<GCDListItemwithUnit> curvedSurface;
    private List<GCDListItemwithUnit> component;
    private List<GCDListItemwithUnit> edge;
    private List<GCDListItemwithUnit> stockTrim;
    private List<GCDListItemwithUnit> displayName;
    private List<GCDListItemwithUnit> name;
    private List<GCDListItemwithUnit> storageType;
    private List<GCDListItemwithUnit> editable;
    private List<GCDListItemwithUnit> unitType;
}