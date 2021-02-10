package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingDetailsTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        CycleTimeValueTrackingTests.class,
        CycleTimeValueTrackingDetailsTests.class
})

public class CiaCirTestDevSuite {
}
