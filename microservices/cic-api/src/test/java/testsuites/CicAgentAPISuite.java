package testsuites;

import com.apriori.cic.api.tests.CicAgentJobPartResultTest;
import com.apriori.cic.api.tests.CicAgentJobResultsTest;
import com.apriori.cic.api.tests.CicAgentRunPartsTest;
import com.apriori.cic.api.tests.CicAgentRunUdaPartsTest;
import com.apriori.cic.api.tests.CicAgentSustainMetricsTest;
import com.apriori.cic.api.tests.CicAgentTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CicAgentJobResultsTest.class,
    CicAgentJobPartResultTest.class,
    CicAgentRunPartsTest.class,
    CicAgentRunUdaPartsTest.class,
    CicAgentTest.class,
    CicAgentSustainMetricsTest.class
})
public class CicAgentAPISuite {
}
