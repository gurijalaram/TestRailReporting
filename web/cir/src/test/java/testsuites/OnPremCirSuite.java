package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.login.LoginTests;
import com.navigation.ReportsNavigationTests;
import com.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.dtcmetrics.plastic.PlasticDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.plastic.PlasticDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.OnPremTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(OnPremTest.class)
@Suite.SuiteClasses({
        ReportsNavigationTests.class,
        LoginTests.class,
        AssemblyDetailsReportTests.class,
        CastingDtcReportTests.class,
        CastingDtcDetailsReportTests.class,
        CastingDtcComparisonReportTests.class,
        MachiningDtcReportTests.class,
        MachiningDtcDetailsReportTests.class,
        MachiningDtcComparisonReportTests.class,
        PlasticDtcReportTests.class,
        PlasticDtcDetailsReportTests.class,
        PlasticDtcComparisonReportTests.class
})

public class OnPremCirSuite {
}