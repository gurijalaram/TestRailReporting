package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "acs/CostResultsProcess.json")
public class CostResultsProcessResponse extends ArrayList {
    private CostResultsProcessItem costResultsProcessItem;
}
