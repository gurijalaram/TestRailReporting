package testsuites;

import com.apriori.MultiPartCostingTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    MultiPartCostingTest.class
})
public class MultiPartSuite {
}
