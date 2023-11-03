package com.apriori.bcs.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Schema(location = "NewReportSchema.json")
@Data
public class NewReport extends Report {
    private NewReport response;
    private String roundToNearest;
}
