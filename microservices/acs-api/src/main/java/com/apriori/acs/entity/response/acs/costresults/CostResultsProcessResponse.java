package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "acs/CostResultsProcess.json")
public class CostResultsProcessResponse extends ArrayList<CostResultsProcessResponse> {
    private CostResultsProcessItem costResultsProcessItem;
}
