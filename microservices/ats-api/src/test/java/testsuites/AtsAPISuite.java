package testsuites;

import com.apriori.AtsAuthenticationTests;
import com.apriori.AtsAuthorization;
import com.apriori.AtsUsersTests;

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
