package testsuites;

import com.apriori.cic.agent.tests.AgentInstallTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AgentInstallTest.class
})
public class AgentInstallSuite {
}
