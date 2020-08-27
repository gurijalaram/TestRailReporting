package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import evaluate.ProcessRoutingTests;
import evaluate.SecondaryProcessTests;
import evaluate.designguidance.dtc.DTCCastingTests;
import evaluate.designguidance.tolerance.ToleranceTests;
import evaluate.materialutilization.MaterialStockTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//TODO: add project ID for TestRail
//@ProjectRunID("999")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    MaterialStockTests.class,
    ToleranceTests.class,
    DTCCastingTests.class,
    ProcessRoutingTests.class,
    SecondaryProcessTests.class,
})
public class AdhocTestSuite {
}