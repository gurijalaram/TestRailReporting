package com.apriori.bcs.models.response;

import com.apriori.annotations.Schema;

import lombok.Data;

@Schema(location = "NewReportSchema.json")
@Data
public class NewReport extends Report {
    private NewReport response;
    private String roundToNearest;
}
