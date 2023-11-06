package com.apriori.nts.api.reports.partscost;

import java.util.Arrays;

public enum PartsCostSheetsEnum {
    PART_COST_REPORT("Part Cost Report"),
    REPORT_SUMMARY_SHEET("ReportSummarySheet"),
    THUMBNAILS("Thumbnails"),
    PARTS_COST_DATA("partCostData");

    private final String reportSheetName;

    PartsCostSheetsEnum(String reportDataName) {
        this.reportSheetName = reportDataName;
    }

    public String getReportSheetName() {
        return reportSheetName;
    }

    public static PartsCostSheetsEnum fromString(String reportSheetName) throws IllegalArgumentException {
        return Arrays.stream(PartsCostSheetsEnum.values())
            .filter(value -> value.reportSheetName.equals(reportSheetName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("'%s' doesn't exist in enum ", reportSheetName)));
    }
}
