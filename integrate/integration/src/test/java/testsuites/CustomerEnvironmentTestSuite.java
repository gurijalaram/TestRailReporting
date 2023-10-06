package testsuites;

import com.apriori.ach.tests.AchMainPageAPITestAPI;
import com.apriori.ach.ui.tests.AchMainPageUITest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AchMainPageUITest.class,
    AchMainPageAPITestAPI.class
})
public class CustomerEnvironmentTestSuite {
}
