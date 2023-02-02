package com.apriori.acs.entity.response.acs.costresults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;

@Data
@Schema(location = "acs/CostResults.json")
@NoArgsConstructor
public class CostResultsRoot {
    private Object processInstanceKey;
    private Object artifactKey;
    private Array gcds;
    private Object resultMapBean;
    private Object propertyInfoMap;
}
