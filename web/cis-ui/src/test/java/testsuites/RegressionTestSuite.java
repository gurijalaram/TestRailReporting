package testsuites;

import com.apriori.messages.MessagesTest;
import com.apriori.navigation.NavigationPanelTest;
import com.apriori.partsandassemblies.PartsAndAssemblyTest;
import com.apriori.partsandassembliesdetails.PartsAndAssembliesDetailsTest;
import com.apriori.projectdetails.ProjectsDetailsTest;
import com.apriori.projects.ProjectsTest;
import com.apriori.userpreference.UserPreferenceTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    NavigationPanelTest.class,
    MessagesTest.class,
    UserPreferenceTest.class,
    PartsAndAssembliesDetailsTest.class,
    PartsAndAssemblyTest.class,
    ProjectsTest.class,
    ProjectsDetailsTest.class
})
public class RegressionTestSuite {
}