package testsuites;

import com.apriori.AgentInstallTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AgentInstallTest.class
})
public class AgentInstallSuite {
}
