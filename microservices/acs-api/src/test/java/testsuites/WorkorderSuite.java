package testsuites;

import com.apriori.acs.api.tests.workorders.WorkorderAPITests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    WorkorderAPITests.class
})
public class WorkorderSuite {
}
