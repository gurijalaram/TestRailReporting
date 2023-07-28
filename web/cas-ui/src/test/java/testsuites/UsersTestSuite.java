package testsuites;

import com.apriori.customer.users.BatchImportListTests;
import com.apriori.customer.users.CustomerStaffTests;
import com.apriori.customer.users.EditUserTests;
import com.apriori.customer.users.NewUserTests;
import com.apriori.customer.users.UsersGrantApplicationAccessTests;
import com.apriori.customer.users.UsersGrantLicenseTests;
import com.apriori.customer.users.UsersStaffAssociationTests;

import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludePackages("com.apriori")
@SelectClasses({
    UsersStaffAssociationTests.class,
    CustomerStaffTests.class,
    NewUserTests.class,
    EditUserTests.class,
    BatchImportListTests.class,
    UsersGrantApplicationAccessTests.class,
    UsersGrantLicenseTests.class})
public class UsersTestSuite {
}
