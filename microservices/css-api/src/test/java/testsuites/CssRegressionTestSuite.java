package testsuites;

import com.apriori.css.api.tests.CssSearchTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CssSearchTests.class
})
public class CssRegressionTestSuite {
}
