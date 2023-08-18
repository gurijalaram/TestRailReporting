package com.apriori.acs.models.response.acs.costresults;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "acs/CostResultsGcd.json")
public class CostResultsGcdResponse extends ArrayList<CostResultsGcdItem> {
}
