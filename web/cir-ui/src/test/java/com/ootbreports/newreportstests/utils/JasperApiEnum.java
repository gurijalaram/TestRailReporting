package com.ootbreports.newreportstests.utils;

import utils.Constants;

public enum JasperApiEnum {
    ASSEMBLY_COST_A4("/general/assemblycost/AssemblyCostA4ReportRequest"),
    ASSEMBLY_COST_LETTER("/general/assemblycost/AssemblyCostLetterReportRequest"),
    ASSEMBLY_DETAILS("/general/assemblydetails/AssemblyDetailsReportRequest"),
    COMPONENT_COST("/componentcost/ComponentCostReportRequest"),
    COST_OUTLIER_IDENTIFICATION("/costoutlieridentification/CostOutlierIdentificationReportRequest"),
    COST_OUTLIER_IDENTIFICATION_DETAILS("/costoutlieridentification/CostOutlierIdentificationDetailsReportRequest"),
    CYCLE_TIME_VALUE_TRACKING("/cycletimevaluetracking/CycleTimeValueTrackingReportRequest"),
    CYCLE_TIME_VALUE_TRACKING_DETAILS("/cycletimevaluetracking/CycleTimeValueTrackingDetailsReportRequest"),
    DESIGN_OUTLIER_IDENTIFICATION("/designoutlieridentification/DesignOutlierIdentificationReportRequest"),
    DESIGN_OUTLIER_IDENTIFICATION_DETAILS("/designoutlieridentification/DesignOutlierIdentificationDetailsReportRequest"),
    CASTING_DTC_COMPARISON("/castingdtc/CastingDtcComparisonReportRequest"),
    CASTING_DTC_DETAILS("/castingdtc/CastingDtcDetailsReportRequest"),
    CASTING_DTC("/castingdtc/CastingDtcReportRequest"),
    MACHINING_DTC_COMPARISON("/machiningdtc/MachiningDtcComparisonReportRequest"),
    MACHINING_DTC_DETAILS("/machiningdtc/MachiningDtcDetailsReportRequest"),
    MACHINING_DTC("/machiningdtc/MachiningDtcReportRequest"),
    PLASTIC_DTC_COMPARISON("/plasticdtc/PlasticDtcComparisonReportRequest"),
    PLASTIC_DTC_DETAILS("/plasticdtc/PlasticDtcDetailsReportRequest"),
    PLASTIC_DTC("/plasticdtc/PlasticDtcReportRequest"),
    SCENARIO_COMPARISON("/scenariocomparison/ScenarioComparisonReportRequest"),
    SHEET_METAL_DTC_COMPARISON("/sheetmetaldtc/SheetMetalDtcComparisonReportRequest"),
    SHEET_METAL_DTC_DETAILS("/sheetmetaldtc/SheetMetalDtcDetailsReportRequest"),
    SHEET_METAL_DTC("/sheetmetaldtc/SheetMetalDtcReportRequest");

    private final String endpoint;

    JasperApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpointString() {
        return endpoint;
    }

    public String getEndpoint(Object... variables) {
        return Constants.API_REPORTS_PATH.concat(getEndpointString());
    }
}
