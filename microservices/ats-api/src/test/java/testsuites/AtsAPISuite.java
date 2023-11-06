package testsuites;

import com.apriori.ats.api.tests.AtsAuthenticationTests;
import com.apriori.ats.api.tests.AtsAuthorization;
import com.apriori.ats.api.tests.AtsUsersTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AtsAuthorization.class,
    AtsAuthenticationTests.class,
    AtsUsersTests.class
})
public class AtsAPISuite {
}
