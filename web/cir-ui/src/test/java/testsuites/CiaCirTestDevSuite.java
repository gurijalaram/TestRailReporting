package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.costoutlieridentification.CostOutlierIdentificationReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        CostOutlierIdentificationReportTests.class
})

public class CiaCirTestDevSuite {
}
