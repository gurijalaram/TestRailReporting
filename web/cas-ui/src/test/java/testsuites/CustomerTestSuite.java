package testsuites;

import com.apriori.customer.AprioriInternalProfileTests;
import com.apriori.customer.CustomerAccessTests;
import com.apriori.customer.CustomersTests;
import com.apriori.customer.EditCustomerTests;
import com.apriori.customer.NewCustomerTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    NewCustomerTests.class,
    EditCustomerTests.class,
    AprioriInternalProfileTests.class,
    CustomersTests.class,
    CustomerAccessTests.class
})
public class CustomerTestSuite {
}
