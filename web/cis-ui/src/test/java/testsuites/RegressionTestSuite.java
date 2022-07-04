package testsuites;

import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.explore.SearchTests;
import com.explore.StartComparisonTests;
import com.navigation.NavigationPanelTest;
import com.partsandassemblies.PartsAndAssemblyTest;
import com.partsandassembliesdetails.PartsAndAssembliesDetailsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("867")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        PartsAndAssemblyTest.class,
        NavigationPanelTest.class,
        PartsAndAssembliesDetailsTest.class
})
public class RegressionTestSuite {
}