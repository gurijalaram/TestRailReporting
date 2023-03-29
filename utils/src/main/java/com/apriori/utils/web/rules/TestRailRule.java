package com.apriori.utils.web.rules;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.web.driver.TestMode;
import com.apriori.utils.web.exceptions.APIClient;
import com.apriori.utils.web.exceptions.APIException;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author - Kunal Patel
 */
public class TestRailRule extends TestWatcher {

    private static final String STATUS_ID = "status_id";
    private static final String COMMENT = "comment";
    private static final Integer FAILED_STATUS = 5;
    private static final String FAILURE_COMMENTS = "Test Executed - Status updated automatically from automation.";
    private static final Integer SUCCESS_STATUS = 1;
    private static final String SUCCESS_COMMENTS = "Test Executed - Status updated automatically from automation.";
    private static final String API_URL = "https://apriori3.testrail.net";
    private static final String USERNAME = "kpatel+1@apriori.com";
    private static final String PASSWORD = "cAnKTFzwhgxS9TJxV09p-9XkAs5FMPEiE352kv0nY";
    private static final String DEFAULT_TEST_MODE = "QA";
    private TestRail testRail = null;

    /*
     * (non-Javadoc)
     *
     * @see org.junit.rules.TestWatcher#starting(org.junit.runner.Description)
     */
    @Override
    protected void starting(Description description) {
        testRail = description.getAnnotation(TestRail.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.junit.rules.TestWatcher#failed(java.lang.Throwable,
     * org.junit.runner.Description)
     */
    @Override
    protected void failed(Throwable t, Description description) {
        generateResultForCase(FAILED_STATUS, FAILURE_COMMENTS);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.junit.rules.TestWatcher#succeeded(org.junit.runner.Description)
     */
    @Override
    protected void succeeded(Description description) {
        generateResultForCase(SUCCESS_STATUS, SUCCESS_COMMENTS);
    }

    private void generateResultForCase(final Integer statusCode, final String comment) {
        if (testRail == null) {
            return;
        }
        HashMap<String, Object> parameterData = new HashMap<>();
        parameterData.put(STATUS_ID, statusCode);
        parameterData.put(COMMENT, comment);
        try {
            addResultForCase(parameterData);
        } catch (APIException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param parameterData
     * @throws IOException
     * @throws APIException This method adds result to TestRail for test cases
     */
    public void addResultForCase(Map<String, Object> parameterData)
        throws IOException, APIException {
        
        // Do not post to Test Rail if mode (aka Test Mode) is Local.
        // This allows for test development/break-fix
        if (System.getProperty("mode", DEFAULT_TEST_MODE).equals(TestMode.QA_LOCAL.value())) {
            return;
        }

        APIClient client = new APIClient(API_URL);
        client.setUser(USERNAME);
        client.setPassword(PASSWORD);
        String[] values = testRail.testCaseId();
        for (String value : values) {
            client.sendPost("add_result_for_case/" + CommonConstants.RUN_ID + "/" + value + "", parameterData);
        }
    }
}