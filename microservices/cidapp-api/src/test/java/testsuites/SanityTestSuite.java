package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.SANITY;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@IncludeTags(SANITY)
public class SanityTestSuite {
}
