package testsuites;

import com.apriori.dfs.api.tests.DigitalFactoriesTests;
import com.apriori.dfs.api.tests.ProcessGroupsTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    DigitalFactoriesTests.class,
    ProcessGroupsTests.class
})
public class DfsApiSuite {
}
