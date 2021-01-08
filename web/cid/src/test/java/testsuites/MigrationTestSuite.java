package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.compare.EditPublicComparisonTests;
import com.evaluate.ProcessGroupsTests;
import com.evaluate.ProcessRoutingTests;
import com.evaluate.SecondaryProcessTests;
import com.evaluate.designguidance.tolerance.ToleranceTests;
import com.explore.ActionsTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.MigrationTests;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(MigrationTests.class)
@Suite.SuiteClasses({
    ProcessGroupsTests.class,
    ProcessRoutingTests.class,
    EditPublicComparisonTests.class,
    ActionsTests.class,
    SecondaryProcessTests.class,
    ToleranceTests.class,
    ProcessGroupsTests.class,

})

public class MigrationTestSuite {
}
