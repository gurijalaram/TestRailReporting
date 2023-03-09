package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "acs/CostResultsGcd.json")
public class CostResultsGcdResponse extends ArrayList<CostResultsGcdResponse> {
    private CostResultsGcdItem costResultsGcdItem;
}
