package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.messages.MessagesTest;
import com.navigation.NavigationPanelTest;
import com.partsandassemblies.PartsAndAssemblyTest;
import com.partsandassembliesdetails.PartsAndAssembliesDetailsTest;
import com.projects.ProjectsTest;
import com.userpreference.UserPreferenceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("2123")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    NavigationPanelTest.class,
    MessagesTest.class,
    UserPreferenceTest.class,
    PartsAndAssembliesDetailsTest.class,
    PartsAndAssemblyTest.class,
    ProjectsTest.class
})
public class RegressionTestSuite {
}