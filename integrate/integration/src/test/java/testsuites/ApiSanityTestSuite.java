package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@IncludeTags(API_SANITY)
public class ApiSanityTestSuite {
}
