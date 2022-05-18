package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.integration.tests.CICIntegrationTests;
import com.integration.tests.CIDIntegrationTests;
import com.integration.tests.CIPIntegrationTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("561")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    CICIntegrationTests.class,
    CIDIntegrationTests.class,
    CIPIntegrationTests.class
})
public class GoNoGoTestSuite {
}
