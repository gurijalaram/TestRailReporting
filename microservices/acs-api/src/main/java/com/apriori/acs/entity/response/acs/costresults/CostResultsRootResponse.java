package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(location = "acs/CostResultsRoot.json")
@NoArgsConstructor
public class CostResultsRootResponse extends ArrayList {
    private CostResultsRootItem costResultsRootItem;
}
