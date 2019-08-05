package main.java.api;

import main.java.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
       BillOfMaterialsTest.class,
        PartOfMaterialsTest.class
})
public class TestSuiteAPI {
}
