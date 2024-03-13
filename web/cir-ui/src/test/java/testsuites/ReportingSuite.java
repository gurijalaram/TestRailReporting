package testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori.cir.ui.tests.ootbreports")
@IncludeTags(REPORTS)
public class ReportingSuite {
}