package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.ON_PREM;

import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludePackages("com.ootbreports")
@IncludeTags(ON_PREM)
public class OnPremCirSuite {
}