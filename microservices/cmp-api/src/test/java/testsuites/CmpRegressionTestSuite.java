package testsuites;

import com.apriori.cmp.api.tests.CmpComparisonTests;
import com.apriori.cmp.api.tests.CmpSaveComparisonTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CmpSaveComparisonTests.class,
    CmpComparisonTests.class
})
public class CmpRegressionTestSuite {
}
