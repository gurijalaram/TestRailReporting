package testsuites;

import com.apriori.cic.api.tests.CicAgentInstallTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CicAgentInstallTest.class
})
public class AgentInstallSuite {
}
