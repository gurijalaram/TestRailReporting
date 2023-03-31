package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.messages.MessagesTest;
import com.navigation.NavigationPanelTest;
import com.partsandassemblies.PartsAndAssemblyTest;
import com.partsandassembliesdetails.PartsAndAssembliesDetailsTest;
import com.userpreference.UserPreferenceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("2123")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    PartsAndAssemblyTest.class,
    NavigationPanelTest.class,
    PartsAndAssembliesDetailsTest.class,
    MessagesTest.class,
    UserPreferenceTest.class
})
public class RegressionTestSuite {
}