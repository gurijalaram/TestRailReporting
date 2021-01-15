package com.apriori.cis.entity.request;

public class ReportParameters {
    private Boolean roundToDollar;
    private String currencyCode;
    private String partNumber;
    private String exportSetName;
    private String scenarioName;

    public Boolean getRoundToDollar() {
        return roundToDollar;
    }

    public ReportParameters setRoundToDollar(Boolean roundToDollar) {
        this.roundToDollar = roundToDollar;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public ReportParameters setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public ReportParameters setPartNumber(String partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public String getExportSetName() {
        return exportSetName;
    }

    public ReportParameters setExportSetName(String exportSetName) {
        this.exportSetName = exportSetName;
        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public ReportParameters setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }
}
