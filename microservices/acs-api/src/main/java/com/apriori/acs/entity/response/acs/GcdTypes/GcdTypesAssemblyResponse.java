package com.apriori.acs.entity.response.acs.GcdTypes;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GcdTypesAssembly.json")
public class GcdTypesAssemblyResponse {
    @JsonProperty("AxiGroove")
    private List<GcdListItemWithUnit> axiGroove;
    @JsonProperty("AssemblyComponentGcd")
    private List<GcdListItemWithUnit> assemblyComponentGcd;
    @JsonProperty("Stage")
    private List<GcdListItemWithUnit> stage;
    @JsonProperty("SimpleHole")
    private List<GcdListItemWithUnit> simpleHole;
    @JsonProperty("ContactRegion")
    private List<GcdListItemWithUnit> contactRegion;
    @JsonProperty("ContactGroup")
    private List<GcdListItemWithUnit> contactGroup;
    @JsonProperty("Component")
    private List<GcdListItemWithUnit> component;
    @JsonProperty("Weld")
    private List<GcdListItemWithUnit> weld;
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
