package testsuites;

import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.ootbreports")
@SelectClasses(AssemblyDetailsReportTests.class)
public class CiaCirTestDevSuite {
}
