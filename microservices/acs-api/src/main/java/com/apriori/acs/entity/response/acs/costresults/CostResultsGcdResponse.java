package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(location = "acs/CostResultsGcd.json")
public class CostResultsGcdResponse extends ArrayList<CostResultsGcdItem> {
}
