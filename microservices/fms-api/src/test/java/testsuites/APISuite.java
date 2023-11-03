package testsuites;

import com.apriori.fms.api.tests.FileManagementControllerTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    FileManagementControllerTest.class
})
public class APISuite {
}
