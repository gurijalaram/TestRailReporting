package testsuites;

import com.apriori.shared.util.tests.AuthorizationUtilTest;
import com.apriori.shared.util.tests.AwsParameterStoreUtilTest;
import com.apriori.shared.util.tests.PropertiesContextTest;
import com.apriori.shared.util.tests.http.models.request.HTTPRequestTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AuthorizationUtilTest.class,
    AwsParameterStoreUtilTest.class,
    PropertiesContextTest.class,
    HTTPRequestTest.class
})
public class SharedUtilSuite {
}
