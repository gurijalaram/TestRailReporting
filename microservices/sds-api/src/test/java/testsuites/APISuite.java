package testsuites;

import com.apriori.ApFilesTest;
import com.apriori.ComponentsTest;
import com.apriori.ConnectionsTest;
import com.apriori.CostingTemplatesTest;
import com.apriori.FeatureDecisionsTests;
import com.apriori.IterationsTest;
import com.apriori.PublishAssembliesTests;
import com.apriori.ScenarioAssociationsTest;
import com.apriori.ScenarioIterationsTest;
import com.apriori.ScenariosTest;
import com.apriori.SecondaryProcessesTest;
import com.apriori.SustainabilityScenarioTest;

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

