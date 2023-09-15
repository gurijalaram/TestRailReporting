package testsuites;

import com.apriori.shared.util.AuthorizationUtilTest;
import com.apriori.shared.util.AwsParameterStoreUtilTest;
import com.apriori.shared.util.PropertiesContextTest;
import com.apriori.shared.util.http.models.request.HTTPRequestTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
//    AuthorizationUtilTest.class,
//    AwsParameterStoreUtilTest.class,
//    PropertiesContextTest.class,
    HTTPRequestTest.class
})
public class SharedUtilSuite {
}
