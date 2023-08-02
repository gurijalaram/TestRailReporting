package com.apriori.cic.models.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Schema(location = "PlmPartResponseSchema.json")
public class PlmPartResponse {
    @JsonProperty("@odata.context")
    public String context;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssZ.class)
    @JsonProperty("CreatedOn")
    private LocalDateTime createdOn;
    @JsonProperty("ID")
    public String id;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssZ.class)
    @JsonProperty("LastModified")
    public LocalDateTime lastModified;
    @JsonProperty("AlternateNumber")
    public Integer alternateNumber;
    @JsonProperty("AnnualVolume")
    public Integer annualVolume;
    @JsonProperty("ApCycleTime")
    public Double apCycleTime;
    @JsonProperty("ApDFMRating")
    public String apDFMRating;
    @JsonProperty("ApDFMRiskScore")
    public Object apDFMRiskScore;
    @JsonProperty("ApFBC")
    public Double apFBC;
    @JsonProperty("ApMaterial")
    public String apMaterial;
    @JsonProperty("ApPG")
    public String apPG;
    @JsonProperty("ApPLMPrimaryPlantLocation")
    public String apPLMPrimaryPlantLocation;
    @JsonProperty("ApPPC")
    public Double apPPC;
    @JsonProperty("AssemblyMode")
    public PlmPartAttribute assemblyMode;
    @JsonProperty("BOMType")
    public String bomType;
    @JsonProperty("BatchSize")
    public Integer batchSize;
    @JsonProperty("Boolean1")
    public Boolean boolean1;
    @JsonProperty("CIDLink")
    public String cidLink;
    @JsonProperty("CabinetName")
    public String cabinetName;
    @JsonProperty("CalcReal1")
    public Double calcReal1;
    @JsonProperty("CapitalInvestment")
    public Double capitalInvestment;
    @JsonProperty("ChangeStatus")
    public String changeStatus;
    @JsonProperty("CheckOutStatus")
    public String checkOutStatus;
    @JsonProperty("CheckoutState")
    public String checkoutState;
    @JsonProperty("ColourNumber")
    public Integer colourNumber;
    @JsonProperty("Comments")
    public String comments;
    @JsonProperty("ConfigurableModule")
    public PlmPartAttribute configurableModule;
    @JsonProperty("CostFbc")
    public Double costFbc;
    @JsonProperty("CostPpc")
    public Double costPpc;
    @JsonProperty("CreatedBy")
    public String createdBy;
    @JsonProperty("CurrencyCode")
    public String currencyCode;
    @JsonProperty("Date2")
    public String date2;
    @JsonProperty("DateTime1")
    public String dateTime1;
    @JsonProperty("DefaultTraceCode")
    public PlmPartAttribute defaultTraceCode;
    @JsonProperty("DefaultUnit")
    public PlmPartAttribute defaultUnit;
    @JsonProperty("Description")
    public String description;
    @JsonProperty("EndItem")
    public Object endItem;
    @JsonProperty("FinishMass")
    public Double finishMass;
    @JsonProperty("FolderLocation")
    public String folderLocation;
    @JsonProperty("FolderName")
    public String folderName;
    @JsonProperty("GatheringPart")
    public Object gatheringPart;
    @JsonProperty("GeneralStatus")
    public PlmPartAttribute generalStatus;
    @JsonProperty("Identity")
    public String identity;
    @JsonProperty("Integer1")
    public Integer integer1;
    @JsonProperty("LaborTime")
    public Double laborTime;
    @JsonProperty("Latest")
    public Boolean latest;
    @JsonProperty("LegalValList")
    public String legalValList;
    @JsonProperty("LifeCycleTemplateName")
    public String lifeCycleTemplateName;
    @JsonProperty("MaterialCost")
    public Double materialCost;
    @JsonProperty("ModifiedBy")
    public String modifiedBy;
    @JsonProperty("MultiselectString")
    public String multiselectString;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Number")
    public String number;
    @JsonProperty("ObjectType")
    public String objectType;
    @JsonProperty("OrganizationReference")
    public String organizationReference;
    @JsonProperty("PartSummaryReport")
    public Object partSummaryReport;
    @JsonProperty("PhantomManufacturingPart")
    public Object phantomManufacturingPart;
    @JsonProperty("ProductionLife")
    public Double productionLife;
    @JsonProperty("ProjectNumber")
    public Object projectNumber;
    @JsonProperty("ProjectType")
    public Object projectType;
    @JsonProperty("RadButtonString")
    public Object radButtonString;
    @JsonProperty("Real3")
    public Double real3;
    @JsonProperty("Real4")
    public Double real4;
    @JsonProperty("RealNumber1")
    public Double realNumber1;
    @JsonProperty("RealNumber2")
    public Double realNumber2;
    @JsonProperty("RealNumberUnits1")
    public Double realNumberUnits1;
    @JsonProperty("RealUnits2")
    public Double realUnits2;
    @JsonProperty("Reference1")
    public Double reference1;
    @JsonProperty("Revision")
    public String revision;
    @JsonProperty("RoughMass")
    public Double roughMass;
    @JsonProperty("ScenarioName")
    public String scenarioName;
    @JsonProperty("ShareStatus")
    public String shareStatus;
    @JsonProperty("Source")
    public PlmPartAttribute source;
    @JsonProperty("State")
    public PlmPartAttribute state;
    @JsonProperty("StrEmail2")
    public String strEmail2;
    @JsonProperty("String1")
    public String string1;
    @JsonProperty("String4")
    public String string4;
    @JsonProperty("Supersedes")
    public Object supersedes;
    @JsonProperty("TypeIcon")
    public PlmPartAttribute typeIcon;
    @JsonProperty("URL1")
    public String url1;
    @JsonProperty("Utilization")
    public Double utilization;
    @JsonProperty("VPE")
    public String vpe;
    @JsonProperty("Version")
    public String version;
    @JsonProperty("VersionID")
    public String versionID;
    @JsonProperty("View")
    public String view;
}
