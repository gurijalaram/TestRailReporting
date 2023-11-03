package testsuites;

import com.apriori.gcd.api.tests.GcdTreesTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    GcdTreesTests.class
})
public class GcdRegressionTestSuite {
}
