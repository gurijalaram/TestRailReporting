package com.apriori.acs.entity.response.acs.GcdTypes;

import com.apriori.utils.http.enums.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GcdTypesCastingInvestment.json")
public class GcdTypesCastingInvestmentResponse {
    @JsonProperty("CoreBundle")
    private List<GcdListItemWithUnit> coreBundle;
    @JsonProperty("PlanarFace")
    private List<GcdListItemWithUnit> planarFace;
    @JsonProperty("SimpleHole")
    private List<GcdListItemWithUnit> simpleHole;
    @JsonProperty("MultiStepHole")
    private List<GcdListItemWithUnit> multiStepHole;
    @JsonProperty("NotSupported")
    private List<GcdListItemWithUnit> notSupported;
    @JsonProperty("ComboVoid")
    private List<GcdListItemWithUnit> comboVoid;
    @JsonProperty("SlideBundle")
    private List<GcdListItemWithUnit> slideBundle;
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
    @JsonProperty("Void")
    private List<GcdListItemWithUnit> Void;
    @JsonProperty("CurvedSurface")
    private List<GcdListItemWithUnit> curvedSurface;
    @JsonProperty("Component")
    private List<GcdListItemWithUnit> component;
    @JsonProperty("SharpEdge")
    private List<GcdListItemWithUnit> sharpEdge;
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
