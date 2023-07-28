package testsuites;

import com.apriori.GcdTreesTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    GcdTreesTests.class
})
public class GcdRegressionTestSuite {
}
