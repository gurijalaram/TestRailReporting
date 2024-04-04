package testsuites;

import com.apriori.report.api.tests.ReportTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    ReportTests.class,
})
public class ReportingReplicaTestSuite {
}
