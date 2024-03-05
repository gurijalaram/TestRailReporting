package com.apriori.report.api.models;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class ReportBean {
    @CsvBindByName
    private Integer id;
    @CsvBindByName(column = "scenario_name")
    private String scenarioName;
    @CsvBindByName(column = "part_number")
    private String partNumber;
    @CsvBindByName
    private String description;
    @CsvBindByName(column = "costing_message")
    private String costingMessage;
    @CsvBindByName(column = "costed_timestamp")
    private String costedTimestamp;
    @CsvBindByName
    private Double fbc;
    @CsvBindByName(column = "annual_volume")
    private Integer annualVolume;
    @CsvBindByName(column = "material_id")
    private Integer materialId;
    @CsvBindByName(column = "process_group_id")
    private Integer processGroupId;
    @CsvBindByName(column = "production_life")
    private Double productionLife;
    @CsvBindByName(column = "full_name")
    private String fullName;
}
