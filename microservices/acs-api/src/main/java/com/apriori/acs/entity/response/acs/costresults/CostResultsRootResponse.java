package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Schema(location = "acs/CostResultsRoot.json")
public class CostResultsRootResponse extends ArrayList<CostResultsRootItem> {
}
