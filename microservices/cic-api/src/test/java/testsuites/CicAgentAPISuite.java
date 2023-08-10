package testsuites;

import com.apriori.CicAgentJobPartResultTest;
import com.apriori.CicAgentJobResultsTest;
import com.apriori.CicAgentRunPartsTest;
import com.apriori.CicAgentRunUdaPartsTest;
import com.apriori.CicAgentTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CicAgentJobResultsTest.class,
    CicAgentJobPartResultTest.class,
    CicAgentRunPartsTest.class,
    CicAgentRunUdaPartsTest.class,
    CicAgentTest.class
})
public class CicAgentAPISuite {
}
