package com.apriori.cid.api.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REGRESSION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.models.response.component.PostComponentResponse;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ComponentRedirectTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private ComponentInfoBuilder component;

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14197)
    @Description("Verify receipt of 301 response when getting component details of a file which already exists")
    public void receive301AfterUploadOfExistingComponent() {
        component = new ComponentRequestUtil().getComponent();

        PostComponentResponse existingPartResponse = componentsUtil.postComponentResponse(component);

        component.setComponentIdentity(existingPartResponse.getSuccesses().get(0).getComponentIdentity());
        component.setScenarioIdentity(existingPartResponse.getSuccesses().get(0).getScenarioIdentity());

        assertThat(componentsUtil.getComponentIdentityExpectingStatusCode(component, HttpStatus.SC_MOVED_PERMANENTLY).getBody(), is(emptyString()));
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14440)
    @Description("Verify receipt of 301 response when getting component and scenario details of a file which already exists using new scenario")
    public void receive301AfterUploadOfExistingComponentWithNewScenario() {

        component = new ComponentRequestUtil().getComponent();

        PostComponentResponse existingPartResponse = componentsUtil.postComponentResponse(component);

        component.setComponentIdentity(existingPartResponse.getSuccesses().get(0).getComponentIdentity());
        component.setScenarioIdentity(existingPartResponse.getSuccesses().get(0).getScenarioIdentity());

        assertThat(scenariosUtil.getScenarioExpectingStatusCode(component, HttpStatus.SC_MOVED_PERMANENTLY).getBody(), is(emptyString()));
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14444)
    @Description("Verify receipt of 301 response when getting component and scenario details of a file which already exists using new scenario")
    public void receive301AfterUploadOfExistingComponentWithOverriddenScenario() {

        component = new ComponentRequestUtil().getComponent();

        PostComponentResponse existingPartScenarioResponse = componentsUtil.postComponentResponse(component);

        component.setComponentIdentity(existingPartScenarioResponse.getSuccesses().get(0).getComponentIdentity());
        component.setScenarioIdentity(existingPartScenarioResponse.getSuccesses().get(0).getScenarioIdentity());

        assertThat(scenariosUtil.getScenarioExpectingStatusCode(component, HttpStatus.SC_MOVED_PERMANENTLY).getBody(), is(emptyString()));
    }

    @Test
    @Tag(REGRESSION)
    @TestRail(id = 14450)
    @Description("Verify receipt of 301 response when getting iteration details of a file which already exists using new scenario")
    public void receive301IterationsEndpoint() {

        component = new ComponentRequestUtil().getComponent();

        PostComponentResponse existingPartResponse = componentsUtil.postComponentResponse(component);

        component.setComponentIdentity(existingPartResponse.getSuccesses().get(0).getComponentIdentity());
        component.setScenarioIdentity(existingPartResponse.getSuccesses().get(0).getScenarioIdentity());

        assertThat(componentsUtil.getComponentIterationLatestExpectingStatusCode(component, HttpStatus.SC_MOVED_PERMANENTLY).getBody(), is(emptyString()));
    }
}
