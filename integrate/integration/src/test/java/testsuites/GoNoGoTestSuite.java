package testsuites;

import com.integration.tests.CIAIntegrationTests;
import com.integration.tests.CICIntegrationTests;
import com.integration.tests.CIDIntegrationTests;
import com.integration.tests.CIRIntegrationTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CICIntegrationTests.class,
    CIDIntegrationTests.class,
    CIAIntegrationTests.class,
    CIRIntegrationTests.class
})

public class GoNoGoTestSuite {
}
