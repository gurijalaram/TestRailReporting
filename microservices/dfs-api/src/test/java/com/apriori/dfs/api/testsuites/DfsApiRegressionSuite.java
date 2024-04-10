package com.apriori.dfs.api.testsuites;

import com.apriori.dfs.api.tests.DigitalFactoriesTests;
import com.apriori.dfs.api.tests.MaterialStocksTests;
import com.apriori.dfs.api.tests.MaterialsTests;
import com.apriori.dfs.api.tests.ProcessGroupsTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    DigitalFactoriesTests.class,
    ProcessGroupsTests.class,
    MaterialsTests.class,
    MaterialStocksTests.class
})
public class DfsApiRegressionSuite {
}
