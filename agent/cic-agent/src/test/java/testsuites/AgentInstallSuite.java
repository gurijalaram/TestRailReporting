package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import agent.tests.AgentInstallTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("1283")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AgentInstallTest.class,
})
public class AgentInstallSuite {

}
