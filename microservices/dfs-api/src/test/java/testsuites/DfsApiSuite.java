package testsuites;

import com.apriori.dfs.api.tests.DigitalFactoriesTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    DigitalFactoriesTests.class
})
public class DfsApiSuite {
}