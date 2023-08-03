package testsuites;

import static com.apriori.TestSuiteType.TestSuite.SMOKE;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@IncludeTags(SMOKE)
public final class SmokeTestSuite {
}
