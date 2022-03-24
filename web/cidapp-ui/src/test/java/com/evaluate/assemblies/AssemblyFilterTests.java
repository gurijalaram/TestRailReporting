package com.evaluate.assemblies;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class AssemblyFilterTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ComponentsListPage componentsListPage;

    public AssemblyFilterTests() {
        super();
    }

    @Test
    public void testPresetFilter() {
        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadPublishAndOpenAssembly(
                subComponentNames,
                componentExtension,
                processGroupEnum,
                assemblyName,
                assemblyExtension,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter().selectFilter("Assigned To Me")
            .submit(ComponentsListPage.class);

        Stream.of(subComponentNames.toArray())
            .forEach(componentName -> assertThat(componentsListPage.getListOfScenarios(subComponentNames.toString(), scenarioName), is(equalTo(0))));
    }
}
