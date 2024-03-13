package testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS_API;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori.cir.ui.tests.ootbreports.newreportstests")
@IncludeTags(REPORTS_API)
public class ReportsApiTestSuite {
}
