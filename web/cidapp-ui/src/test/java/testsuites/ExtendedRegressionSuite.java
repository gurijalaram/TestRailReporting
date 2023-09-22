package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@IncludeTags(EXTENDED_REGRESSION)
public class ExtendedRegressionSuite {
}
