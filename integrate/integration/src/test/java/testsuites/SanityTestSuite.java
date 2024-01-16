package testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SANITY;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags({API_SANITY, SANITY})
public class SanityTestSuite {
}
