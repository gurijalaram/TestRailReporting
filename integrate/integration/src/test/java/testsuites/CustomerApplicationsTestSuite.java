package testsuites;

import com.apriori.testconfig.TestSuiteType.TestSuite;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CISRegressionTestSuite.class, // aP Workspace
        CICTestSuite.class, // aP Generate
        ReportingSuite.class, // aP Analytics
        SanityTestSuite.class, // aP Design
})
@ExcludeTags({TestSuite.CUSTOMER, TestSuite.NON_CUSTOMER})
public class CustomerApplicationsTestSuite {
}
