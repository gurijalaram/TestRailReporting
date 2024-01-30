package testsuites;

import com.apriori.bcm.api.tests.UpdateWorksheetTests;
import com.apriori.bcm.api.tests.WorksheetTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    WorksheetTests.class,
    UpdateWorksheetTests.class
})
public class RegressionTestSuite {
}
