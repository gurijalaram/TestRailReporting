package testsuites;

import com.apriori.MultiPartCostingTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    MultiPartCostingTest.class
})
public class MultiPartSuite {
}
