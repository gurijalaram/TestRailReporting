package testsuites;

import static com.apriori.TestSuiteType.TestSuite.REPORTS;

import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludePackages("com.ootbreports")
@IncludeTags(REPORTS)
public class ReportingSuite {
}