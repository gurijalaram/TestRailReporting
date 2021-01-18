package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        AssemblyDetailsReportTests.class,
        PlasticDtcDetailsReportTests.class,
        CastingDtcReportTests.class,
        SheetMetalDtcReportTests.class
})

public class CiaCirTestDevSuite {
}
