package testsuites;

import com.apriori.cis.ui.tests.messages.MessagesTest;
import com.apriori.cis.ui.tests.navigation.NavigationPanelTest;
import com.apriori.cis.ui.tests.partsandassemblies.PartsAndAssemblyTest;
import com.apriori.cis.ui.tests.partsandassembliesdetails.PartsAndAssembliesDetailsTest;
import com.apriori.cis.ui.tests.projectdetails.ProjectsDetailsTest;
import com.apriori.cis.ui.tests.projects.ProjectsTest;
import com.apriori.cis.ui.tests.userpreference.UserPreferenceTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    NavigationPanelTest.class,
    MessagesTest.class,
    UserPreferenceTest.class,
    PartsAndAssembliesDetailsTest.class,
    PartsAndAssemblyTest.class,
    ProjectsTest.class,
    ProjectsDetailsTest.class
})
public class CISRegressionTestSuite {
}