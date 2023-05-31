package testsuites;

import com.apriori.gcd.tests.GcdTreesTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("2766")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    GcdTreesTests.class
})

public class GcdRegressionTestSuite {
}
