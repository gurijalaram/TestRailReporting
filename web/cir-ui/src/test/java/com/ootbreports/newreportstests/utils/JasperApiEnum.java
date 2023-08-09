package com.ootbreports.newreportstests.utils;

import utils.Constants;

public enum JasperApiEnum {
    ASSEMBLY_COST_A4("/general/assemblycost/AssemblyCostA4ReportRequest"),
    ASSEMBLY_COST_LETTER("/general/assemblycost/AssemblyCostLetterReportRequest"),
    ASSEMBLY_DETAILS("/general/assemblydetails/AssemblyDetailsReportRequest"),
    CASTING_DTC_COMPARISON("/dtcmetrics/castingdtc/CastingDtcComparisonReportRequest"),
    CASTING_DTC_DETAILS("/dtcmetrics/castingdtc/CastingDtcDetailsReportRequest"),
    CASTING_DTC("/dtcmetrics/castingdtc/CastingDtcReportRequest"),
    COMPONENT_COST("/componentcost/ComponentCostReportRequest"),
    COST_OUTLIER_IDENTIFICATION("/costoutlieridentification/CostOutlierIdentificationReportRequest"),
    COST_OUTLIER_IDENTIFICATION_DETAILS("/costoutlieridentification/CostOutlierIdentificationDetailsReportRequest"),
    CYCLE_TIME_VALUE_TRACKING("/cycletimevaluetracking/CycleTimeValueTrackingReportRequest"),
    CYCLE_TIME_VALUE_TRACKING_DETAILS("/cycletimevaluetracking/CycleTimeValueTrackingDetailsReportRequest"),
    DESIGN_OUTLIER_IDENTIFICATION("/designoutlieridentification/DesignOutlierIdentificationReportRequest"),
    DESIGN_OUTLIER_IDENTIFICATION_DETAILS("/designoutlieridentification/DesignOutlierIdentificationDetailsReportRequest"),
    DIGITAL_FACTORY_PERFORMANCE("/digitalfactoryperformance/DigitalFactoryPerformanceReportRequest"),
    DIGITAL_FACTORY_PERFORMANCE_DETAILS("/digitalfactoryperformance/DigitalFactoryPerformanceDetailsReportRequest"),
    MACHINING_DTC_COMPARISON("/dtcmetrics/machiningdtc/MachiningDtcComparisonReportRequest"),
    MACHINING_DTC_DETAILS("/dtcmetrics/machiningdtc/MachiningDtcDetailsReportRequest"),
    MACHINING_DTC("/dtcmetrics/machiningdtc/MachiningDtcReportRequest"),
    PLASTIC_DTC_COMPARISON("/dtcmetrics/plasticdtc/PlasticDtcComparisonReportRequest"),
    PLASTIC_DTC_DETAILS("/dtcmetrics/plasticdtc/PlasticDtcDetailsReportRequest"),
    PLASTIC_DTC("/dtcmetrics/plasticdtc/PlasticDtcReportRequest"),
    RECOMMENDED_TEST_PARTS("/recommendedtestparts/RecommendedTestPartsReportRequest"),
    POTENTIAL_SAVINGS_VALUE_TRACKING("/potentialsavingsvaluetracking/PotentialSavingsValueTrackingReportRequest"),
    POTENTIAL_SAVINGS_VALUE_TRACKING_DETAILS("/potentialsavingsvaluetracking/PotentialSavingsValueTrackingDetailsReportRequest"),
    SCENARIO_COMPARISON("/scenariocomparison/ScenarioComparisonReportRequest"),
    SHEET_METAL_DTC_COMPARISON("/dtcmetrics/sheetmetaldtc/SheetMetalDtcComparisonReportRequest"),
    SHEET_METAL_DTC_DETAILS("/dtcmetrics/sheetmetaldtc/SheetMetalDtcDetailsReportRequest"),
    SHEET_METAL_DTC("/dtcmetrics/sheetmetaldtc/SheetMetalDtcReportRequest"),
    SPEND_ANALYSIS_VALUE_TRACKING("/spendanalysisvaluetracking/SpendAnalysisValueTrackingReportRequest"),
    SPEND_ANALYSIS_VALUE_TRACKING_DETAILS("/spendanalysisvaluetracking/SpendAnalysisValueTrackingDetailsReportRequest"),
    SPEND_ANALYSIS_VALUE_TRACKING_SIMPLIFIED("/spendanalysisvaluetracking/SpendAnalysisValueTrackingSimplifiedReportRequest"),
    SPEND_ANALYSIS_VALUE_TRACKING_DETAILS_SIMPLIFIED("/spendanalysisvaluetracking/SpendAnalysisValueTrackingDetailsSimplifiedReportRequest"),
    TARGET_COST_TREND("/targetcosttrend/TargetCostTrendReportRequest"),
    TARGET_COST_VALUE_TRACKING("/targetcosttrend/TargetCostValueTrackingReportRequest"),
    TARGET_COST_VALUE_TRACKING_DETAILS("/targetcosttrend/TargetCostValueTrackingDetailsReportRequest"),
    TARGET_QUOTED_COST_TREND("/targetquotedcosttrend/TargetAndQuotedCostTrendReportRequest"),
    TARGET_QUOTED_COST_VALUE_TRACKING("/targetquotedcosttrend/TargetAndQuotedCostValueTrackingReportRequest"),
    TARGET_QUOTED_COST_VALUE_TRACKING_DETAILS("/targetquotedcosttrend/TargetAndQuotedCostValueTrackingDetailsReportRequest");

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
