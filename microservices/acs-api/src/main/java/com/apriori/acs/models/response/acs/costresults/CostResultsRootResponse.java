package com.apriori.acs.models.response.acs.costresults;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "acs/CostResultsRoot.json")
public class CostResultsRootResponse extends ArrayList<CostResultsRootItem> {
}
