package com.apriori.cir.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum CirApiEnum implements ExternalEndpointEnum {
    DTC_METRICS_REPORT_START("reports/aPriori/reports/DTC Metrics/"),
    APRIORI_REPORTS_START("reports/aPriori/reports/"),
    APRIORI_DEPLOYMENT_LEADER_REPORTS(APRIORI_REPORTS_START.getEndpointString().concat("deploymentLeader/")),
    APRIORI_GENERAL_REPORTS(APRIORI_REPORTS_START.getEndpointString().concat("general/")),
    APRIORI_SOLUTIONS_REPORTS(APRIORI_REPORTS_START.getEndpointString().concat("solutions/")),
    APRIORI_SOLUTIONS_QUOTING_REPORTS(APRIORI_SOLUTIONS_REPORTS.getEndpointString().concat("quoting/")),
    APRIORI_SOLUTIONS_SOURCING_REPORTS(APRIORI_SOLUTIONS_REPORTS.getEndpointString().concat("sourcing/")),
    APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS(APRIORI_SOLUTIONS_REPORTS.getEndpointString().concat("designToCost/")),
    APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORT_TARGET_QUOTED_COST(APRIORI_SOLUTIONS_REPORTS.getEndpointString().concat("/designToCost/targetAndQuotedCost/")),
    APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORT_TARGET_QUOTED_COST_1("targetAndQuotedCost/"),
    APRIORI_UPGRADE_PROCESS_REPORTS(APRIORI_REPORTS_START.getEndpointString().concat("Upgrade%sProcess/")),

    ASSEMBLY_COST_A4(APRIORI_GENERAL_REPORTS.getEndpointString().concat("assemblyCost.a4/inputControls/exportSetName;partNumber;scenarioName;exportDate;currencyCode/values?freshData=false")),
    ASSEMBLY_COST_LETTER(APRIORI_GENERAL_REPORTS.getEndpointString().concat("assemblyCost.letter/inputControls/exportSetName;partNumber;scenarioName;exportDate;currencyCode/values?freshData=false")),
    ASSEMBLY_DETAILS(APRIORI_GENERAL_REPORTS.getEndpointString().concat("assemblyDetails/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;allExportIds;createdBy;lastModifiedBy;assemblyNumber;").concat(
        "scenarioName;assemblySelect;currencyCode/values?freshData=false")),
    BASIC_COST_AVOIDANCE(APRIORI_GENERAL_REPORTS.getEndpointString().concat("basicAvoidanceReport/inputControls/earliestCostDate;latestCostDate;costMetric;currencyCode;sortOrder;componentsCriteria/values?freshData=false&includeTotalCount=true")),
    CASTING_DTC_COMPARISON(DTC_METRICS_REPORT_START.getEndpointString().concat("casting/castingDTCComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;").concat(
        "massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false")),
    CASTING_DTC_DETAILS(DTC_METRICS_REPORT_START.getEndpointString().concat("casting/castingDTCDetails/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;").concat(
            "massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false")),
    CASTING_DTC(DTC_METRICS_REPORT_START.getEndpointString().concat("casting/castingDTC/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;massMetric;sortOrder;currencyCode;").concat(
        "annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;outlierDistance;partsSelect/values")),
    COMPONENT_COST(APRIORI_GENERAL_REPORTS.getEndpointString().concat("componentCost/inputControls/exportSetName;componentType;latestExportDate;createdBy;lastModifiedBy;componentNumber;scenarioName;").concat(
        "componentSelect;componentCostCurrencyCode/values?freshData=false")),
    COST_OUTLIER_IDENTIFICATION(APRIORI_SOLUTIONS_SOURCING_REPORTS.getEndpointString().concat("costOutlierIdentification/inputControls/useLatestExport;earliestExportDate;latestExportDate;").concat(
        "exportSetName;rollup;currencyCode;costMetric;sortOrder;componentCostMin;componentCostMax;annualizedPotentialThreshold;percentDifferenceThreshold/values?freshData=false")),
    COST_OUTLIER_IDENTIFICATION_DETAILS(APRIORI_SOLUTIONS_SOURCING_REPORTS.getEndpointString().concat("costOutlierIdentificationDetails/inputControls/useLatestExport;earliestExportDate;").concat(
        "latestExportDate;exportSetName;rollup;currencyCode;costMetric;sortOrder;componentCostMin;componentCostMax;annualizedPotentialThreshold;percentDifferenceThreshold/values?freshData=false")),
    CYCLE_TIME_VALUE_TRACKING(APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS.getEndpointString().concat("cycleTime/cycleTimeValueTracking/inputControls/projectRollup;exportDate;currencyCode/values?freshData=false&includeTotalCount=true")),
    CYCLE_TIME_VALUE_TRACKING_DETAILS(APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS.getEndpointString().concat("cycleTime/cycleTimeValueTrackingDetails/inputControls/projectRollup;projectName;exportDate;currencyCode/values?freshData=false")),
    DESIGN_OUTLIER_IDENTIFICATION(APRIORI_SOLUTIONS_SOURCING_REPORTS.getEndpointString().concat("designOutlierIdentification/inputControls/earliestExportDate;latestExportDate;exportSetName;rollup;exportDate;costMetric;massMetric;aPrioriCostMin;").concat(
        "aPrioriCostMax;aPrioriMassMin;aPrioriMassMax;outlierLines;currencyCode/values?freshData=false")),
    DESIGN_OUTLIER_IDENTIFICATION_DETAILS(APRIORI_SOLUTIONS_SOURCING_REPORTS.getEndpointString().concat("designOutlierIdentificationDetails/inputControls/earliestExportDate;latestExportDate;exportSetName;rollup;exportDate;costMetric;").concat(
        "massMetric;aPrioriCostMin;aPrioriCostMax;aPrioriMassMin;aPrioriMassMax;currencyCode/values")),
    DIGITAL_FACTORY_PERFORMANCE(APRIORI_SOLUTIONS_QUOTING_REPORTS.getEndpointString().concat("vpePerformance/inputControls/earliestExportDate;latestExportDate;exportSetName;rollup;exportDate;costMetric;performanceMetric;annualVolumeMin;").concat(
        "annualVolumeMax;processGroup;processName;materialName;vpeName;currencyCode/values?freshData=false")),
    DIGITAL_FACTORY_PERFORMANCE_DETAILS(APRIORI_SOLUTIONS_QUOTING_REPORTS.getEndpointString().concat("vpePerformanceDetailsInternal/inputControls/currencyCode;annualVolumeMax;annualVolumeMin;performanceMetric;costMetric;").concat(
        "latestExportDate;earliestExportDate;rollup;exportDate;exportSetName/values?freshData=false")),
    MACHINING_DTC_COMPARISON(DTC_METRICS_REPORT_START.getEndpointString().concat("machining/machiningDTCComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;").concat(
        "massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false")),
    MACHINING_DTC_DETAILS(DTC_METRICS_REPORT_START.getEndpointString().concat("machining/machiningDTCDetails/inputControls/useLatestExport;earliestExportDate;latestExportDate;").concat(
        "exportSetName;rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false")),
    MACHINING_DTC(DTC_METRICS_REPORT_START.getEndpointString().concat("machining/machiningDTC/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;").concat(
        "processGroup;exportEventId;metricStatistic;dtcScore;outlierDistance;partsSelect/values?freshData=false")),
    PLASTIC_DTC_COMPARISON(DTC_METRICS_REPORT_START.getEndpointString().concat("plastic/plasticDTCComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;").concat(
        "rollup;costMetric;massMetric;includeDraftIssues;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false")),
    PLASTIC_DTC_DETAILS(DTC_METRICS_REPORT_START.getEndpointString().concat("plastic/plasticDTCDetails/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;massMetric;").concat(
        "includeDraftIssues;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false")),
    PLASTIC_DTC(DTC_METRICS_REPORT_START.getEndpointString().concat("plastic/plasticDTC/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;massMetric;").concat(
        "includeDraftIssues;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;outlierDistance;partsSelect/values?freshData=false")),
    POTENTIAL_SAVINGS_VALUE_TRACKING(APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS.getEndpointString().concat("potentialSavings/potentialSavingsValueTracking/inputControls/projectRollup;exportDate;costMetric;currencyCode/values?freshData=false&includeTotalCount=true")),
    POTENTIAL_SAVINGS_VALUE_TRACKING_DETAILS(APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS.getEndpointString().concat("potentialSavings/potentialSavingsValueTrackingDetails/inputControls/projectRollup;projectName;exportDate;costMetric;currencyCode/values?freshData=false")),
    RECOMMENDED_TEST_PARTS(APRIORI_UPGRADE_PROCESS_REPORTS.getEndpointString().concat("recommendedTestParts/inputControls/useLatestExport;").concat(
        "earliestExportDate;latestExportDate;exportSetName;exportEventId;processGroup;materials;processes;numberOfTopValuesShown/values?freshData=false&includeTotalCount=true")),
    REPORT_EXECUTIONS("reportExecutions"),
    REPORT_EXPORT_BY_REQUEST_ID("reportExecutions/%s/exports"),
    REPORT_OUTPUT_STATUS_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/status"),
    REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/outputResource"),
    REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/attachments/reportComponents.json"),
    SCENARIO_ACTIVITY(APRIORI_DEPLOYMENT_LEADER_REPORTS.getEndpointString().concat("scenarioActivity/inputControls/exportSetName;startDate;endDate;trendingPeriod;department;location;costSource/values/pagination?freshData=false&includeTotalCount=true")),
    SCENARIO_ACTIVITY_DIGITAL_FACTORY_ACTIVITY(APRIORI_DEPLOYMENT_LEADER_REPORTS.getEndpointString().concat("VPEActivity/inputControls/exportSetName;startDate;endDate;department;location;trendingPeriod;costSource/values/pagination?freshData=false&includeTotalCount=true")),
    SCENARIO_ACTIVITY_MATERIAL_ACTIVITY(APRIORI_DEPLOYMENT_LEADER_REPORTS.getEndpointString().concat("materialActivity/inputControls/exportSetName;startDate;endDate;department;location;trendingPeriod;costSource/values/pagination?freshData=false&includeTotalCount=true")),
    SCENARIO_ACTIVITY_PROCESS_ACTIVITY(APRIORI_DEPLOYMENT_LEADER_REPORTS.getEndpointString().concat("processActivity/inputControls/exportSetName;startDate;endDate;department;location;trendingPeriod;costSource/values/pagination?freshData=false&includeTotalCount=true")),
    SCENARIO_ACTIVITY_PROCESS_GROUP_ACTIVITY(APRIORI_DEPLOYMENT_LEADER_REPORTS.getEndpointString().concat("processGroupsActivity/inputControls/exportSetName;startDate;endDate;department;location;trendingPeriod;costSource/values/pagination?freshData=false&includeTotalCount=true")),
    SCENARIO_ACTIVITY_TOTAL_ACTIVITY(APRIORI_DEPLOYMENT_LEADER_REPORTS.getEndpointString().concat("totalActivity/inputControls/exportSetName;startDate;endDate;department;location;trendingPeriod/values/pagination?freshData=false&includeTotalCount=true")),
    SCENARIO_COMPARISON(APRIORI_GENERAL_REPORTS.getEndpointString().concat("scenarioComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;allExportIds;componentType;createdBy;lastModifiedBy;partNumber;scenarioName;scenarioToCompareIDs;scenarioIDs;").concat(
        "currencyCode/values?freshData=false")),
    SPEND_ANALYSIS_VALUE_TRACKING(APRIORI_SOLUTIONS_SOURCING_REPORTS.getEndpointString().concat("spendAnalysisValueTracking/inputControls/projectRollup;exportDate;costMetric;currencyCode/values")),
    SPEND_ANALYSIS_VALUE_TRACKING_DETAILS(APRIORI_SOLUTIONS_SOURCING_REPORTS.getEndpointString().concat("spendAnalysisValueTrackingDetails/inputControls/projectRollup;projectName;exportDate;costMetric;currencyCode/values?freshData=false")),
    SPEND_ANALYSIS_VALUE_TRACKING_DETAILS_SIMPLIFIED(APRIORI_SOLUTIONS_SOURCING_REPORTS.getEndpointString().concat("spendAnalysisValueTrackingSimplifiedDetails/inputControls/projectRollup;projectName;exportDate;costMetric;currencyCode/values?freshData=false&includeTotalCount=true")),
    SPEND_ANALYSIS_VALUE_TRACKING_SIMPLIFIED(APRIORI_SOLUTIONS_SOURCING_REPORTS.getEndpointString().concat("spendAnalysisValueTrackingSimplified/inputControls/projectRollup;exportDate;costMetric;currencyCode/values?freshData=false&includeTotalCount=true")),
    SHEET_METAL_DTC_COMPARISON(DTC_METRICS_REPORT_START.getEndpointString().concat("sheet%sMetal/sheetMetalDTCComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;").concat(
        "rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false")),
    SHEET_METAL_DTC_DETAILS(DTC_METRICS_REPORT_START.getEndpointString().concat("sheet%sMetal/sheetMetalDTCDetails/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;").concat(
        "rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false")),
    SHEET_METAL_DTC(DTC_METRICS_REPORT_START.getEndpointString().concat("sheet%sMetal/sheetMetalDTC/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;").concat(
        "rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;outlierDistance;partsSelect/values?freshData=false")),
    TARGET_COST_TREND(APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS.getEndpointString().concat("targetCost/targetCostTrend/inputControls/projectRollup;projectName;exportDate;costMetric;currencyCode/values?freshData=false&includeTotalCount=true")),
    TARGET_COST_VALUE_TRACKING(APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS.getEndpointString().concat("targetCost/targetCostValueTracking/inputControls/projectRollup;exportDate;costMetric;currencyCode/values?freshData=false&includeTotalCount=true")),
    TARGET_COST_VALUE_TRACKING_DETAILS(APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS.getEndpointString().concat("targetCost/targetCostValueTrackingDetails/inputControls/projectRollup;projectName;exportDate;costMetric;currencyCode;currentProjectId;currentProjectName/values".concat(
        "?freshData=false&includeTotalCount=true"))),
    TARGET_AND_QUOTED_COST_TREND(APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS.getEndpointString().concat("targetAndQuotedCost/".concat("targetAndQuotedCostTrend/inputControls/projectRollup;projectName;exportDate;costMetric;sortOrder;".concat(
        "currencyCode/values?freshData=false&includeTotalCount=true")))),
    TARGET_AND_QUOTED_COST_VALUE_TRACKING(APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS.getEndpointString().concat("targetAndQuotedCost/".concat("targetAndQuotedCostValueTracking/inputControls/projectRollup;exportDate;costMetric;sortOrder;".concat(
        "currencyCode/values?freshData=false&includeTotalCount=true")))),
    TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS(APRIORI_SOLUTIONS_DESIGN_TO_COST_REPORTS.getEndpointString().concat("targetAndQuotedCost/".concat("targetAndQuotedCostValueTrackingDetailsInternal/inputControls/currentProjectName;".concat(
        "currentProjectId;currencyCode;sortOrder;costMetric;projectRollup;projectName;exportDate/values?freshData=false")))),
    UPGRADE_COMPARISON(APRIORI_UPGRADE_PROCESS_REPORTS.getEndpointString().concat("upgradeComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;rollupNew;exportEventId;processGroup;changeLevel;costMetricsLowThreshold;costMetricsHighThreshold;".concat(
        "timeMetricsLowThreshold;timeMetricsHighThreshold;currencyCode/values?freshData=false"))),
    UPGRADE_PART_COMPARISON(APRIORI_UPGRADE_PROCESS_REPORTS.getEndpointString().concat("upgradePartComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;rollupNew;exportEventId;processGroup;partNumber;partNumberNew;changeLevel;".concat(
        "costMetricsLowThreshold;costMetricsHighThreshold;timeMetricsLowThreshold;timeMetricsHighThreshold;currencyCode/values?freshData=false")));

    private final String endpoint;

    CirApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("reports.ui_url") + "rest_v2/" + String.format(getEndpointString(), variables);
    }
}
