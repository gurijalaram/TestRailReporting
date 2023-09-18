package testsuites;

import com.apriori.AccountsControllerTest;
import com.apriori.BillOfMaterialsTest;
import com.apriori.LineItemsTest;
import com.apriori.PartsTest;
import com.apriori.ReportsTest;
import com.apriori.UsersTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
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
