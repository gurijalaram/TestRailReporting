package testsuites;

import com.apriori.sds.api.tests.ApFilesTest;
import com.apriori.sds.api.tests.ComponentsTest;
import com.apriori.sds.api.tests.ConnectionsTest;
import com.apriori.sds.api.tests.CostingTemplatesTest;
import com.apriori.sds.api.tests.FeatureDecisionsTests;
import com.apriori.sds.api.tests.IterationsTest;
import com.apriori.sds.api.tests.PublishAssembliesTests;
import com.apriori.sds.api.tests.ScenarioAssociationsTest;
import com.apriori.sds.api.tests.ScenarioIterationsTest;
import com.apriori.sds.api.tests.ScenariosTest;
import com.apriori.sds.api.tests.SecondaryProcessesTest;
import com.apriori.sds.api.tests.SustainabilityScenarioTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    ApFilesTest.class,
    ComponentsTest.class,
    ConnectionsTest.class,
    CostingTemplatesTest.class,
    IterationsTest.class,
    ScenarioAssociationsTest.class,
    ScenarioIterationsTest.class,
    ScenariosTest.class,
    SecondaryProcessesTest.class,
    PublishAssembliesTests.class,
    FeatureDecisionsTests.class,
    SustainabilityScenarioTest.class
})
public class APISuite {
}

