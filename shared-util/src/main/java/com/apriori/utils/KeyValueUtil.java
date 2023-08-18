package com.apriori.utils;

import com.apriori.exceptions.KeyValueException;
import com.apriori.http.utils.QueryParams;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyValueUtil {

    /**
     * Accepts a string of key/value pair, processes and converts them to QueryParams
     *
     * @param paramKeysValues - the key, value pair
     * @param charToSplit     - the character to separate the key and value
     * @return QueryParams
     */
    public QueryParams keyValue(String[] paramKeysValues, String charToSplit) {
        Map<String, String> paramMap = new HashMap<>();
        QueryParams queryParams = new QueryParams();
        List<String[]> paramKeyValues = Arrays.stream(paramKeysValues).map(o -> o.split(charToSplit))
            .collect(Collectors.toList());

        try {
            paramKeyValues.forEach(o -> paramMap.put(o[0].trim(), o[1].trim()));
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new KeyValueException(ae.getMessage(), paramKeyValues);
        }

        return queryParams.use(paramMap);
    }
}
