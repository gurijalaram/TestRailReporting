package main.java.api.edc.suite;

import main.java.api.edc.AccountsTest;
import main.java.api.edc.BillOfMaterialsTest;
import main.java.api.edc.PartOfMaterialsTest;
import main.java.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
       BillOfMaterialsTest.class,
       PartOfMaterialsTest.class,
       AccountsTest.class
})
public class TestSuiteAPI {
}
