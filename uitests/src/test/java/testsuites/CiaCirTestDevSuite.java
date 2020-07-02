package testsuites;

import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcTests;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        PlasticDtcTests.class
})

public class CiaCirTestDevSuite {
}
