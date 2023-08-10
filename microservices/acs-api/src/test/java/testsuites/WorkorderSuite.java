package testsuites;

import com.apriori.workorders.WorkorderAPITests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    WorkorderAPITests.class
})
public class WorkorderSuite {
}
