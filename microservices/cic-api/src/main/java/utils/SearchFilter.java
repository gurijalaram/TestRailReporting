package utils;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter {

    private static String dataValue;
    private static List<String> buildParams = new ArrayList<>();

    public SearchFilter buildParameter(String dataValue) {
        buildParams.add(dataValue);
        return this;
    }

    public SearchFilter build() {
        return this;
    }

    public String[] getQueryParams() {
        return buildParams.stream().toArray(String[]::new);
    }
}
