package testsuites;

import cireporttests.login.LoginTests;
import cireporttests.navigation.NavigationTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.apriori.utils.runner.CategorySuiteRunner;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        LoginTests.class,
        NavigationTests.class,
        ciadmintests.navigation.NavigationTests.class,
        AssemblyDetailsReportTests.class,
        MachiningDtcReportTests.class,
        CastingDtcReportTests.class,
        CastingDtcComparisonReportTests.class,
        CastingDtcDetailsReportTests.class
})
public class CiaCirTestDevSuite {
}
