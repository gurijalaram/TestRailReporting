package test.java.testsuites;

import main.java.api.AccountsTest;
import main.java.api.BillOfMaterialsTest;
import main.java.api.PartOfMaterialsTest;
import main.java.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        AccountsTest.class,
        BillOfMaterialsTest.class,
        PartOfMaterialsTest.class
})
public class APITestSuite {
}
