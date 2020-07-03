package testsuites;

import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        AssemblyDetailsReportTests.class
})

public class CiaCirTestDevSuite {
}
