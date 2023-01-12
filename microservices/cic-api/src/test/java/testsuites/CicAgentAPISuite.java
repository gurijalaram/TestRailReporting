package testsuites;

import com.apriori.cic.tests.CicAgentJobPartResultTest;
import com.apriori.cic.tests.CicAgentJobResultsTest;
import com.apriori.cic.tests.CicAgentRunPartsTest;
import com.apriori.cic.tests.CicAgentTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("1283")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    CicAgentTest.class,
    CicAgentRunPartsTest.class,
    CicAgentJobResultsTest.class,
    CicAgentJobPartResultTest.class
})
public class CicAgentAPISuite {
}
