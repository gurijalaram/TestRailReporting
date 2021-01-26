package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.dtcmetrics.castingdtc.CastingDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        CastingDtcDetailsReportTests.class,
        MachiningDtcReportTests.class,
        PlasticDtcReportTests.class,
        SheetMetalDtcReportTests.class
})

public class CiaCirTestDevSuite {
}
