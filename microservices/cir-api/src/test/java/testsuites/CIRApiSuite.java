package testsuites;

import com.apriori.JasperReportTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    JasperReportTest.class
})
public class CIRApiSuite {
}