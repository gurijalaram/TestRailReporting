package common.testdata;

import com.apriori.pagedata.WorkFlowData;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.json.utils.JsonManager;

import utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * deserialize the test data json file to page data class objects.
 */
public class TestDataService {

    public Map<String, Object> testDataMap = new HashMap<String, Object>();

    /**
     * Enum used as a paremeter to define map collection
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

    public WorkFlowData getTestData() {

        WorkFlowData workFlowData = (WorkFlowData) JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream("testdata/WorkFlowTestData.json"), WorkFlowData.class);
        workFlowData.setWorkflowName(UIUtils.saltString(workFlowData.getWorkflowName()));
        return workFlowData;
    }

    public Map deserializeDataToMap(String inputJsonFile) {
        return JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream("testdata/" + inputJsonFile), HashMap.class);
    }

    public WorkFlowData getTestData(String dataFile) {
        WorkFlowData workFlowData = (WorkFlowData) JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream("testdata/" + dataFile), WorkFlowData.class);
        workFlowData.setWorkflowName(UIUtils.saltString(workFlowData.getWorkflowName()));
        return workFlowData;
    }
}