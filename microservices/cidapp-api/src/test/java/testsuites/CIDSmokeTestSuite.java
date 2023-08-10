package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.evaluate.CostAllCadTests;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags(SMOKE)
@SelectClasses({
    CostAllCadTests.class
})
public class CIDSmokeTestSuite {
}
