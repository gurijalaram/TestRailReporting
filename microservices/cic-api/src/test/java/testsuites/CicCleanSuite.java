package testsuites;

import com.apriori.cic.api.tests.CicWorkflowsCleanTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CicWorkflowsCleanTest.class
})
public class CicCleanSuite {
}
