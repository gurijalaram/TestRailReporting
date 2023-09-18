package testsuites;

import com.apriori.AtsAuthenticationTests;
import com.apriori.AtsAuthorization;
import com.apriori.AtsUsersTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    AtsAuthorization.class,
    AtsAuthenticationTests.class,
    AtsUsersTests.class
})
public class AtsAPISuite {
}
