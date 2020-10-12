package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcComparisonReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcDetailsReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pageobjects.pages.view.reports.AssemblyDetailsReportPage;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        AssemblyDetailsReportTests.class,
        CastingDtcReportTests.class
})

public class CiaCirTestDevSuite {
}
