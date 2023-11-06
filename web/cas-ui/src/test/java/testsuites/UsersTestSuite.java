package testsuites;

import com.apriori.cas.ui.tests.customer.users.BatchImportListTests;
import com.apriori.cas.ui.tests.customer.users.CustomerStaffTests;
import com.apriori.cas.ui.tests.customer.users.EditUserTests;
import com.apriori.cas.ui.tests.customer.users.NewUserTests;
import com.apriori.cas.ui.tests.customer.users.UsersGrantApplicationAccessTests;
import com.apriori.cas.ui.tests.customer.users.UsersGrantLicenseTests;
import com.apriori.cas.ui.tests.customer.users.UsersStaffAssociationTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
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
