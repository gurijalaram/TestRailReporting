package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.explore.SearchTests;
import com.explore.StartComparisonTests;
import com.navigation.NavigationPanelTest;
import com.partsandassemblies.PartsAndAssemblyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("867")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        PartsAndAssemblyTest.class,
        NavigationPanelTest.class
})
public class RegressionTestSuite {
}