package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS_API;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags(REPORTS_API)
//@SelectPackages("com.ootbreports.newreportstests")
public class ReportsApiTestSuite {
}
