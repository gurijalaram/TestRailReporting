package testsuites;

import com.apriori.CicAgentJobPartResultTest;
import com.apriori.CicAgentJobResultsTest;
import com.apriori.CicAgentRunPartsTest;
import com.apriori.CicAgentTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    CicAgentJobResultsTest.class,
    CicAgentJobPartResultTest.class,
    CicAgentRunPartsTest.class,
    CicAgentRunUdaPartsTest.class,
    CicAgentTest.class
})
public class CicAgentAPISuite {
}
