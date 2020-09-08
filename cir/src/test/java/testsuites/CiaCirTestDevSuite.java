package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import navigation.ReportsNavigationTests;
import ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        ReportsNavigationTests.class
})

public class CiaCirTestDevSuite {
}
