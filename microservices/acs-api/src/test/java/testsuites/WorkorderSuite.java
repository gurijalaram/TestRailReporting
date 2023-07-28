package testsuites;

import com.apriori.workorders.WorkorderAPITests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    WorkorderAPITests.class
})
public class WorkorderSuite {
}
