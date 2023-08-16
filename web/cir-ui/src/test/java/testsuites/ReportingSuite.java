package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS;

import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.jupiter.api.Tag;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludePackages("com.ootbreports")
@ExcludeTags(REPORTS)
@SelectClasses({
    AssemblyDetailsReportTests.class
})
public class ReportingSuite {
}