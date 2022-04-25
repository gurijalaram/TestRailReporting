package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.explore.SearchTests;
import com.explore.StartComparisonTests;
import com.partsandassemblies.PartsAndAssemblyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("867")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        StartComparisonTests.class,
        SearchTests.class,
        PartsAndAssemblyTest.class
})
public class RegressionTestSuite {
}