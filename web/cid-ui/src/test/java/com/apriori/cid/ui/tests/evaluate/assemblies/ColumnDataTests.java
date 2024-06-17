package com.apriori.cid.ui.tests.evaluate.assemblies;

import com.apriori.cid.api.models.response.PersonResponse;
import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.PeopleUtil;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ColumnDataTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ExplorePage explorePage;
    private ComponentsTablePage componentsTablePage;
    private CssComponent cssComponent = new CssComponent();

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

        String hingeIdentity = cssComponent.findFirst(hinge_assembly, scenarioName, currentUser).getScenarioIdentity();
        String bigRingIdentity = cssComponent.findFirst(big_ring, scenarioName, currentUser).getScenarioIdentity();
        String pinIdentity = cssComponent.findFirst(pin, scenarioName, currentUser).getScenarioIdentity();
        String smallRingIdentity = cssComponent.findFirst(small_ring, scenarioName, currentUser).getScenarioIdentity();

        softAssertions.assertThat(explorePage.getColumnData(ColumnsEnum.ANNUAL_VOLUME, hingeIdentity, currentUser)).isEqualTo("500");

        componentsTablePage = explorePage.navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView();

        PersonResponse person = new PeopleUtil().getCurrentPerson(currentUser);

        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.PROCESS_GROUP, bigRingIdentity, currentUser)).isEqualTo(ProcessGroupEnum.FORGING.getProcessGroup());
        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.DIGITAL_FACTORY, bigRingIdentity, currentUser)).isEqualTo(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());
        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.LAST_UPDATED_BY, pinIdentity, currentUser))
            .isEqualTo(person.getGivenName() + " " + person.getFamilyName());
        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.TOTAL_CAPITAL_INVESTMENT, smallRingIdentity, currentUser)).isNotEqualTo("21700.29");
        softAssertions.assertThat(componentsTablePage.getColumnData(ColumnsEnum.FINISH_MASS, smallRingIdentity, currentUser)).isNotEqualTo("0.03kg");

        softAssertions.assertAll();
    }
}
