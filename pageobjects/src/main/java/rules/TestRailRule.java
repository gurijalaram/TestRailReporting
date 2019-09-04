package main.java.rules;

import main.java.utils.TestRail;
import main.java.utils.gurock.testrail.APIClient;
import main.java.utils.gurock.testrail.APIException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author - Kunal Patel
 */
public class TestRailRule extends TestWatcher {

    private TestRail testRail;
    private static final String STATUS_ID = "status_id";
    private static final String COMMENT = "comment";
    private static final Integer FAILED_STATUS = 5;
    private static final String FAILURE_COMMENTS = "Test Executed - Status updated automatically from automation.";
    private static final Integer SUCCESS_STATUS = 1;
    private static final String SUCCESS_COMMENTS = "Test Executed - Status updated automatically from automation.";
    private static final String API_URL = "https://apriori3.testrail.net";
    private static final String USERNAME = "kpatel+1@apriori.com";
    private static final String PASSWORD = "cAnKTFzwhgxS9TJxV09p-9XkAs5FMPEiE352kv0nY";
    private static final Integer RUN_ID = 177;

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
        if (testRail.testCaseId() == null) {
            return;
        }
        HashMap<String, Object> parameterData = new HashMap<>();
        parameterData.put(STATUS_ID, FAILED_STATUS);
        parameterData.put(COMMENT, FAILURE_COMMENTS);
        try {
            addResultForCase(parameterData);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.junit.rules.TestWatcher#succeeded(org.junit.runner.Description)
     */
    @Override
    protected void succeeded(Description description) {
        if (testRail.testCaseId() == null) {
            return;
        }
        HashMap<String, Object> parameterData = new HashMap<>();
        parameterData.put(STATUS_ID, SUCCESS_STATUS);
        parameterData.put(COMMENT, SUCCESS_COMMENTS);
        try {
            addResultForCase(parameterData);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (APIException e) {
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
        APIClient client = new APIClient(API_URL);
        client.setUser(USERNAME);
        client.setPassword(PASSWORD);
        String[] values = testRail.testCaseId();
        for (int noOfTCs = 0; noOfTCs < values.length; noOfTCs++) {
            client.sendPost("add_result_for_case/" + RUN_ID + "/"
                + values[noOfTCs] + "", parameterData);
        }
    }
}