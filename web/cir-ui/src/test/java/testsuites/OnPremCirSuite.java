package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.ON_PREM;

import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@IncludePackages("com.ootbreports")
@IncludeTags(ON_PREM)
public class OnPremCirSuite {
}