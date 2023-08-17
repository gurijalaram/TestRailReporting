package com.apriori.cir.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum CirApiEnum implements ExternalEndpointEnum {
    ASSEMBLY_COST_A4("reports/aPriori/reports/general/assemblyCost.a4/inputControls/exportSetName;partNumber;scenarioName;exportDate;currencyCode/values?freshData=false&selectedOnly=true"),
    ASSEMBLY_COST_LETTER("reports/aPriori/reports/general/assemblyCost.letter/inputControls/exportSetName;partNumber;scenarioName;exportDate;currencyCode/values?freshData=false&selectedOnly=true"),
    ASSEMBLY_DETAILS("reports/aPriori/reports/general/assemblyDetails/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;allExportIds;createdBy;lastModifiedBy;assemblyNumber;".concat(
        "scenarioName;assemblySelect;currencyCode/values?freshData=false&selectedOnly=true")),
    BASIC_COST_AVOIDANCE("reports/aPriori/reports/general/basicAvoidanceReport/inputControls/earliestCostDate;latestCostDate;costMetric;currencyCode;sortOrder;componentsCriteria/values?freshData=false&includeTotalCount=true"),
    CASTING_DTC("reports/aPriori/reports/DTC%sMetrics/casting/castingDTC/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;massMetric;sortOrder;currencyCode;".concat(
        "annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;outlierDistance;partsSelect/values")),
    CASTING_DTC_COMPARISON("reports/aPriori/reports/DTC%sMetrics/casting/castingDTCComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;".concat(
        "massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false&selectedOnly=true")),
    CASTING_DTC_DETAILS("reports/aPriori/reports/DTC%sMetrics/casting/castingDTCDetails/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;".concat(
        "massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false&selectedOnly=true")),
    COMPONENT_COST("reports/aPriori/reports/general/componentCost/inputControls/exportSetName;componentType;latestExportDate;createdBy;lastModifiedBy;componentNumber;scenarioName;".concat(
        "componentSelect;componentCostCurrencyCode/values?freshData=false&selectedOnly=true")),
    COST_OUTLIER_IDENTIFICATION("reports/aPriori/reports/solutions/sourcing/costOutlierIdentification/inputControls/useLatestExport;earliestExportDate;latestExportDate;".concat(
        "exportSetName;rollup;currencyCode;costMetric;sortOrder;componentCostMin;componentCostMax;annualizedPotentialThreshold;percentDifferenceThreshold/values?freshData=false&selectedOnly=true")),
    COST_OUTLIER_IDENTIFICATION_DETAILS("reports/aPriori/reports/solutions/sourcing/costOutlierIdentificationDetails/inputControls/useLatestExport;earliestExportDate;".concat(
        "latestExportDate;exportSetName;rollup;currencyCode;costMetric;sortOrder;componentCostMin;componentCostMax;annualizedPotentialThreshold;percentDifferenceThreshold/values?freshData=false&selectedOnly=true")),
    CYCLE_TIME_VALUE_TRACKING("reports/aPriori/reports/solutions/designToCost/cycleTime/cycleTimeValueTracking/inputControls/projectRollup;exportDate;currencyCode/values?freshData=false&includeTotalCount=true"),
    CYCLE_TIME_VALUE_TRACKING_DETAILS("reports/aPriori/reports/solutions/designToCost/cycleTime/cycleTimeValueTrackingDetails/inputControls/projectRollup;projectName;exportDate;currencyCode/values?freshData=false&selectedOnly=true"),
    DESIGN_OUTLIER_IDENTIFICATION("reports/aPriori/reports/solutions/sourcing/designOutlierIdentification/inputControls/earliestExportDate;latestExportDate;exportSetName;rollup;exportDate;costMetric;massMetric;aPrioriCostMin;".concat(
        "aPrioriCostMax;aPrioriMassMin;aPrioriMassMax;outlierLines;currencyCode/values?freshData=false&selectedOnly=true")),
    DESIGN_OUTLIER_IDENTIFICATION_DETAILS("reports/aPriori/reports/solutions/sourcing/designOutlierIdentificationDetails/inputControls/earliestExportDate;latestExportDate;exportSetName;rollup;exportDate;costMetric;".concat(
        "massMetric;aPrioriCostMin;aPrioriCostMax;aPrioriMassMin;aPrioriMassMax;currencyCode/values")),
    DIGITAL_FACTORY_PERFORMANCE("reports/aPriori/reports/solutions/quoting/vpePerformance/inputControls/earliestExportDate;latestExportDate;exportSetName;rollup;exportDate;costMetric;performanceMetric;annualVolumeMin;".concat(
        "annualVolumeMax;processGroup;processName;materialName;vpeName;currencyCode/values?freshData=false")),
    DIGITAL_FACTORY_PERFORMANCE_DETAILS("reports/aPriori/reports/solutions/quoting/vpePerformanceDetailsInternal/inputControls/currencyCode;annualVolumeMax;annualVolumeMin;performanceMetric;costMetric;".concat(
        "latestExportDate;earliestExportDate;rollup;exportDate;exportSetName/values?freshData=false&selectedOnly=true")),
    MACHINING_DTC("reports/DTC%Metrics/machining/machiningDTC/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;".concat(
        "processGroup;exportEventId;metricStatistic;dtcScore;outlierDistance;partsSelect/values?freshData=false&selectedOnly=true")),
    MACHINING_DTC_COMPARISON("reports/aPriori/reports/DTC%sMetrics/machining/machiningDTCComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;".concat(
        "massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false&selectedOnly=true")),
    MACHINING_DTC_DETAILS("reports/aPriori/reports/DTC%sMetrics/machining/machiningDTCDetails/inputControls/useLatestExport;earliestExportDate;latestExportDate;".concat(
        "exportSetName;rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false&selectedOnly=true")),
    PLASTIC_DTC("reports/aPriori/reports/DTC%sMetrics/plastic/plasticDTC/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;massMetric;".concat(
        "includeDraftIssues;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;outlierDistance;partsSelect/values?freshData=false&selectedOnly=true")),
    PLASTIC_DTC_COMPARISON("reports/aPriori/reports/DTC%sMetrics/plastic/plasticDTCComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;".concat(
        "rollup;costMetric;massMetric;includeDraftIssues;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false&selectedOnly=true")),
    PLASTIC_DTC_DETAILS("reports/aPriori/reports/DTC%sMetrics/plastic/plasticDTCDetails/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;massMetric;".concat(
        "includeDraftIssues;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false&selectedOnly=true")),
    POTENTIAL_SAVINGS_VALUE_TRACKING("reports/aPriori/reports/solutions/designToCost/potentialSavings/potentialSavingsValueTracking/inputControls/projectRollup;exportDate;costMetric;currencyCode/values?freshData=false&includeTotalCount=true"),
    POTENTIAL_SAVINGS_VALUE_TRACKING_DETAILS("reports/aPriori/reports/solutions/designToCost/potentialSavings/potentialSavingsValueTrackingDetails/inputControls/projectRollup;projectName;exportDate;costMetric;currencyCode/values?freshData=false&selectedOnly=true"),
    RECOMMENDED_TEST_PARTS("reports/aPriori/reports/Upgrade%sProcess/recommendedTestParts/inputControls/useLatestExport;".concat(
        "earliestExportDate;latestExportDate;exportSetName;exportEventId;processGroup;materials;processes;numberOfTopValuesShown/values?freshData=false&includeTotalCount=true")),
    REPORT_EXECUTIONS("reportExecutions"),
    REPORT_EXPORT_BY_REQUEST_ID("reportExecutions/%s/exports"),
    REPORT_OUTPUT_STATUS_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/status"),
    REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/outputResource"),
    REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/attachments/reportComponents.json"),
    SCENARIO_COMPARISON("reports/aPriori/reports/general/scenarioComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;allExportIds;componentType;createdBy;lastModifiedBy;partNumber;scenarioName;scenarioToCompareIDs;scenarioIDs;".concat(
        "currencyCode/values?freshData=false&selectedOnly=true")),
    SPEND_ANALYSIS_VALUE_TRACKING("reports/aPriori/reports/solutions/sourcing/spendAnalysisValueTracking/inputControls/projectRollup;exportDate;costMetric;currencyCode/values"),
    SPEND_ANALYSIS_VALUE_TRACKING_DETAILS("reports/aPriori/reports/solutions/sourcing/spendAnalysisValueTrackingDetails/inputControls/projectRollup;projectName;exportDate;costMetric;currencyCode/values?freshData=false&selectedOnly=true"),
    SPEND_ANALYSIS_VALUE_TRACKING_DETAILS_SIMPLIFIED("reports/aPriori/reports/solutions/sourcing/spendAnalysisValueTrackingSimplifiedDetails/inputControls/projectRollup;projectName;exportDate;costMetric;currencyCode/values?freshData=false&includeTotalCount=true"),
    SPEND_ANALYSIS_VALUE_TRACKING_SIMPLIFIED("reports/aPriori/reports/solutions/sourcing/spendAnalysisValueTrackingSimplified/inputControls/projectRollup;exportDate;costMetric;currencyCode/values?freshData=false&includeTotalCount=true"),
    SHEET_METAL_DTC("reports/aPriori/reports/DTC%sMetrics/sheet%Metal/sheetMetalDTC/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;".concat(
        "rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;outlierDistance;partsSelect/values?freshData=false&selectedOnly=true")),
    SHEET_METAL_DTC_COMPARISON("reports/aPriori/reports/DTC%sMetrics/sheet%Metal/sheetMetalDTCComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;".concat(
        "rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false&selectedOnly=true")),
    SHEET_METAL_DTC_DETAILS("reports/aPriori/reports/DTC%sMetrics/sheet%Metal/sheetMetalDTCDetails/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;".concat(
        "rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;partsSelect/values?freshData=false&selectedOnly=true")),
    TARGET_COST_TREND("reports/aPriori/reports/solutions/designToCost/targetCost/targetCostTrend/inputControls/projectRollup;projectName;exportDate;costMetric;currencyCode/values?freshData=false&includeTotalCount=true"),
    TARGET_COST_VALUE_TRACKING("reports/aPriori/reports/solutions/designToCost/targetCost/targetCostValueTracking/inputControls/projectRollup;exportDate;costMetric;currencyCode/values?freshData=false&includeTotalCount=true"),
    TARGET_COST_VALUE_TRACKING_DETAILS("reports/aPriori/reports/solutions/designToCost/targetCost/targetCostValueTrackingDetails/inputControls/projectRollup;projectName;exportDate;costMetric;currencyCode;currentProjectId;currentProjectName/values?freshData=false&includeTotalCount=true"),
    TARGET_AND_QUOTED_COST_TREND("reports/aPriori/reports/solutions/designToCost/targetAndQuotedCost/targetAndQuotedCostTrend/inputControls/projectRollup;projectName;exportDate;costMetric;sortOrder;currencyCode/values?freshData=false&includeTotalCount=true"),
    TARGET_AND_QUOTED_COST_VALUE_TRACKING("reports/aPriori/reports/solutions/designToCost/targetAndQuotedCost/targetAndQuotedCostValueTracking/inputControls/projectRollup;exportDate;costMetric;sortOrder;currencyCode/values?freshData=false&includeTotalCount=true"),
    TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS("reports/aPriori/reports/solutions/designToCost/targetAndQuotedCost/targetAndQuotedCostValueTrackingDetailsInternal/inputControls/currentProjectName;currentProjectId;currencyCode;sortOrder;costMetric;projectRollup;projectName;exportDate/values?freshData=false&selectedOnly=true"),
    UPGRADE_COMPARISON("reports/aPriori/reports/Upgrade%sProcess/upgradeComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;rollupNew;exportEventId;processGroup;changeLevel;costMetricsLowThreshold;costMetricsHighThreshold;timeMetricsLowThreshold;timeMetricsHighThreshold;currencyCode/values?freshData=false&selectedOnly=true"),
    UPGRADE_PART_COMPARISON("reports/aPriori/reports/Upgrade%20Process/upgradePartComparison/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;rollupNew;exportEventId;processGroup;partNumber;partNumberNew;changeLevel;costMetricsLowThreshold;costMetricsHighThreshold;timeMetricsLowThreshold;timeMetricsHighThreshold;currencyCode/values?freshData=false&selectedOnly=true");

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
