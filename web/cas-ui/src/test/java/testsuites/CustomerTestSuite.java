package testsuites;

import com.apriori.customer.AprioriInternalProfileTests;
import com.apriori.customer.CustomerAccessTests;
import com.apriori.customer.CustomersTests;
import com.apriori.customer.EditCustomerTests;
import com.apriori.customer.NewCustomerTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    NewCustomerTests.class,
    EditCustomerTests.class,
    AprioriInternalProfileTests.class,
    CustomersTests.class,
    CustomerAccessTests.class
})
public class CustomerTestSuite {
}
