package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS;

import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags(ON_PREM)
@SelectClasses({
    AssemblyDetailsReportTests.class
})
public class ReportingSuite {
}