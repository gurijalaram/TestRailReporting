package testsuites;

import com.apriori.cic.tests.CicAgentTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("1283")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    CicAgentTest.class
})
public class CicAgentAPISuite {
}
