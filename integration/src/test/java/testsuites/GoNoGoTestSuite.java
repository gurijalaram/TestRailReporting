package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.integration.tests.CIAIntegrationTests;
import com.integration.tests.CICIntegrationTests;
import com.integration.tests.CIDIntegrationTests;
import com.integration.tests.CIRIntegrationTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("561")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    CICIntegrationTests.class,
    CIDIntegrationTests.class,
    CIAIntegrationTests.class,
    CIRIntegrationTests.class
})
public class GoNoGoTestSuite {
}
