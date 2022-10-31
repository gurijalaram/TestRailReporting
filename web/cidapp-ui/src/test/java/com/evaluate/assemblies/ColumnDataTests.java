package com.evaluate.assemblies;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.PeopleUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ColumnDataTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ExplorePage explorePage;
    private ComponentsTablePage componentsTablePage;

    public ColumnDataTests() {
        super();
    }

    @Test
    public void columnDataTests() {
        final String hinge_assembly = "Hinge assembly";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".SLDASM";
        final String big_ring = "big ring";
        final String pin = "Pin";
        final String small_ring = "small ring";
        final String subComponentExtension = ".SLDPRT";
        final List<String> subComponentNames = Arrays.asList(big_ring, pin, small_ring);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        SoftAssertions softAssertions = new SoftAssertions();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            hinge_assembly,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .selectFilter("Recent");

        softAssertions.assertThat(explorePage.getColumnData("hinge assembly", scenarioName, ColumnsEnum.ANNUAL_VOLUME, currentUser)).isEqualTo("5,500");

        componentsTablePage = explorePage.navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView();

        softAssertions.assertThat(componentsTablePage.getColumnData(big_ring, scenarioName, ColumnsEnum.PROCESS_GROUP, currentUser)).isEqualTo(ProcessGroupEnum.FORGING.getProcessGroup());
        softAssertions.assertThat(componentsTablePage.getColumnData(big_ring, scenarioName, ColumnsEnum.DIGITAL_FACTORY, currentUser)).isEqualTo(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());
        softAssertions.assertThat(componentsTablePage.getColumnData(pin, scenarioName, ColumnsEnum.LAST_UPDATED_BY, currentUser))
                 .isEqualTo(new PeopleUtil().getCurrentPerson(currentUser).getGivenName());
        softAssertions.assertThat(componentsTablePage.getColumnData(small_ring, scenarioName, ColumnsEnum.TOTAL_CAPITAL_INVESTMENT, currentUser)).isNotEqualTo("21700.29");
        softAssertions.assertThat(componentsTablePage.getColumnData(small_ring, scenarioName, ColumnsEnum.FINISH_MASS, currentUser)).isNotEqualTo("0.03kg");

        softAssertions.assertAll();
    }
}
