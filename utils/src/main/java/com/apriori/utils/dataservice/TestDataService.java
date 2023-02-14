package com.apriori.utils.dataservice;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.reader.file.InitFileData;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.part.PartUtil;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * deserialize the test data json file to page data class objects.
 */
@Slf4j
public class TestDataService {

    public Map<String, Object> testDataMap = new HashMap<String, Object>();

    /**
     * Enum used as a parameter to define map collection
     * when setting map values through the setMapValue method
     */

    public String testCaseID;

    public TestDataService(String testCaseID) {
        this.testCaseID = testCaseID;
    }

    public TestDataService() {
    }

    public void setInputData(Map<String, Object> inputValues) {
        //Create the input values
        this.testDataMap = inputValues;
    }

    /**
     * Getter to return the Input Values Map
     *
     * @return Map
     */
    public Map getInputData() {
        return this.testDataMap;
    }

    public <T> T getTestData(String inputFile, Class<T> klass) {
        T testData = null;
        testData = (T) JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream("testdata/" + inputFile), klass);
        return testData;
    }

    /**
     * Get part test data from css-test-parts.csv file AWS S3 bucket
     *
     * @return PartData class object
     */
    public List<PartData> getPartsFromCloud(Integer numOfParts) {
        List<PartData> partDataList = new ArrayList<>();
        for (int i = 0; i < numOfParts; i++) {
            PartData partData = PartUtil.getPartDataWithFile();
            partDataList.add(partData);
        }
        return partDataList;
    }

    public Map deserializeDataToMap(String inputJsonFile) {
        return JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream("testdata/" + inputJsonFile), HashMap.class);
    }

    /**
     * Export test data to CSV file in {@Link REMOTE_SHARED_PATH path}
     *
     * @param fileName - creates filename to export.
     */
    public void exportDataToCloud(String fileName) {
        File fileToExport = writeToFile(fileName);
        FileResourceUtil.uploadCloudFile("gonogo", fileToExport);
    }

    /**
     * Export test data to CSV file in {@Link REMOTE_SHARED_PATH path}
     *
     * @param fileName - creates filename to export.
     */
    public void deleteCloudFile(String fileName) {
        FileResourceUtil.deleteCloudFile("gonogo", fileName);
    }

    /**
     * verify expected data by reading exported CSV file
     *
     * @param fileName      - file to be read
     * @param expectedValue - string to be matched
     * @return - boolean
     */
    public Boolean verifyCloudFile(String fileName, String expectedValue) {
        List<String[]> fileContent = FileResourceUtil.getCloudFileContent("gonogo", fileName);

        for (String[] line : fileContent) {
            if (Arrays.stream(line).filter(x -> x.contains(expectedValue)).findFirst().isPresent()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Deserialize the test data and return java POJO
     *
     * @param fileName - data file in test resources (MultipleComponentsSummary or PartCostReport)
     * @param klass    - return class name
     * @param <T>      - return class type
     * @return return class of matching class name
     */
    public <T> T getReportData(String fileName, Class<T> klass) {
        T value = null;
        try {
            if (fileName.contains("MultipleComponentsSummary")) {
                log.info(String.format("Getting Content Properties For Document Type: %s", fileName));
                value = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream("testdata/" + fileName), klass);
            }

            if (fileName.contains("PartCostReport")) {
                log.info(String.format("Getting Content Properties For Document Type: %s", fileName));
                value = JsonManager.deserializeJsonFromInputStream(FileResourceUtil.getResourceFileStream("testdata/" + fileName), klass);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    private File writeToFile(String fileName) {
        File fileDirectory = FileResourceUtil.createTempDir("gonogo");
        File fileToExport = new File(String.format("%s/%s", fileDirectory, fileName));
        try {
            FileWriter writer = new FileWriter(fileToExport, true);
            String[] header = (String[]) this.getInputData().keySet().toArray(new String[this.getInputData().size()]);
            String[] dataSet = (String[]) this.getInputData().values().toArray(new String[this.getInputData().size()]);

            // create CSVWriter object filewriter object as parameter
            CSVWriter csvWriter = new CSVWriter(writer);

            // adding data to csv
            csvWriter.writeNext(header);
            csvWriter.writeNext(dataSet);

            // closing writer connection
            writer.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return fileToExport;
    }


}
