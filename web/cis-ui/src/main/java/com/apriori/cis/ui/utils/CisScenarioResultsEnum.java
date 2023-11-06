package com.apriori.cis.ui.utils;

public enum CisScenarioResultsEnum {

    TOTAL_COST("Total Cost"),
    SCENARIO_INPUTS("Scenario Inputs"),
    MATERIAL("Material"),
    MANUFACTURING("Manufacturing"),
    ADDITIONAL_COST("Additional Cost"),
    TOTAL_CAPITAL_EXPENSES("Total Capital Expenses"),
    PIECE_PART_COST("Piece Part Cost"),
    DIGITAL_FACTORY("Digital Factory"),
    PROCESS_GROUP("Process Group"),
    ANNUAL_VOLUME("Annual Volume"),
    BATCH_SIZE("Batch Size"),
    FINISH_MASS("Finish Mass"),
    MATERIAL_COST("Material Cost"),
    MATERIAL_OVERHEAD("Material Overhead"),
    MATERIAL_UNIT_COST("Material Unit Cost"),
    ROUGH_MASS("Rough Mass"),
    UTILIZATION("Utilization"),
    CYCLE_TIME("Cycle Time"),
    DIRECT_OVERHEAD_COST("Direct Overhead Cost"),
    LABOR_COST("Labor Cost"),
    OTHER_DIRECT_COSTS("Other Direct Costs"),
    INDIRECT_OVERHEAD_COST("Indirect Overhead Cost"),
    ROUTING("Routing"),
    AMORTIZED_BATCH_SETUP_COST("Amortized Batch Setup Cost"),
    LOGISTICS("Logistics"),
    MARGIN("Margin"),
    SG_AND_A("SG&A"),
    FIXTURE("Fixture"),
    HARD_TOOLING("Hard Tooling"),
    PROGRAMMING("Programming");

    private final String fields;

    CisScenarioResultsEnum(String fields) {
        this.fields = fields;
    }

    public String getFieldName() {
        return this.fields;
    }
}
