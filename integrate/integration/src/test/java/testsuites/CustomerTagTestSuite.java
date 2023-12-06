package testsuites;

import com.apriori.shared.util.testconfig.TestSuiteType;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
    "com.apriori"
})

@IncludeTags(TestSuiteType.TestSuite.CUSTOMER)
public class CustomerTagTestSuite {

}
