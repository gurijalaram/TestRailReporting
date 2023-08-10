package testsuites;

import com.apriori.CmpComparisonTests;
import com.apriori.CmpSaveComparisonTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CmpSaveComparisonTests.class,
    CmpComparisonTests.class
})
public class CmpRegressionTestSuite {
}
