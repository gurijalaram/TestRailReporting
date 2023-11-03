package testsuites;

import com.apriori.edc.api.tests.AccountsControllerTest;
import com.apriori.edc.api.tests.BillOfMaterialsTest;
import com.apriori.edc.api.tests.LineItemsTest;
import com.apriori.edc.api.tests.PartsTest;
import com.apriori.edc.api.tests.ReportsTest;
import com.apriori.edc.api.tests.UsersTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    BillOfMaterialsTest.class,
    LineItemsTest.class,
    PartsTest.class,
    UsersTest.class,
    AccountsControllerTest.class,
    ReportsTest.class
})
public class RegressionSuite {
}
