package com.apriori.nts.reports.data;

import com.apriori.http.utils.FileResourceUtil;
import com.apriori.json.JsonManager;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ReportData {

    /**
     * Deserialize the test data and return java POJO
     *
     * @param fileName - data file in test resources
     * @param klass    - return class name
     * @param <T>      - return class type
     * @return return class of matching class name
     */
    public static <T> T getExpectedData(String fileName, Class<T> klass) {
        Map<String, Object> reportDetails = new HashMap<>();
        T value = null;
        try {
            if (fileName.contains("MultipleComponentsSummary")) {
                log.info(String.format("Getting Content Properties For Document Type: %s", fileName));
                value = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream("testdata/" + fileName), klass);
            }

            if (fileName.contains("PartCostReport")) {
                log.info(String.format("Getting Content Properties For Document Type: %s", fileName));
                value = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream(fileName), klass);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
