package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectPackages("com.apriori")
@IncludeTags(SMOKE)
public final class SmokeTestSuite {
}
