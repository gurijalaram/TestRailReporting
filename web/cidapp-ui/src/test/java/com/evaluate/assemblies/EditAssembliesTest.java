package com.evaluate.assemblies;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.navtoolbars.InfoPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class EditAssembliesTest extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private InfoPage infoPage;

    final AssemblyUtils assemblyUtils = new AssemblyUtils();

    public EditAssembliesTest() {
        super();
    }

    @Test
    @TestRail(testCaseId = "10810")
    @Description("Shallow Edit an assembly with scenarios uncosted")
    public void testUploadPublishAssemblyAndEdit() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadPublishAndOpenAssembly(
                subComponentNames,
                componentExtension,
                processGroupEnum,
                assemblyName,
                assemblyExtension,
                scenarioName,
                currentUser)
            .editScenario()
            .close(EvaluatePage.class);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.PROCESSING_EDIT_ACTION), is(true));
        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE), is(true));
    }

    @Test
    @TestRail(testCaseId = {"10799", "10768", "1081"})
    @Description("Shallow Edit assembly and scenarios that was cost in CI Design")
    public void testUploadCostPublishAssemblyAndEdit() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly).costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);
        assemblyUtils.publishAssembly(componentAssembly);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PUBLIC), is(true));

        evaluatePage.editScenario()
            .close(EvaluatePage.class);

        assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.PRIVATE), is(true));

        evaluatePage.info()
            .selectStatus("New")
            .inputCostMaturity("Low")
            .inputDescription("QA Test Description")
            .inputNotes("Testing QA notes")
            .submit(EvaluatePage.class);

        evaluatePage.publishScenario()
            .publish(componentAssembly, currentUser, EvaluatePage.class);

        infoPage = evaluatePage.info();
        assertThat(infoPage.getStatus(), is(equalTo("New")));
        assertThat(infoPage.getCostMaturity(), is(equalTo("Low")));
        assertThat(infoPage.getDescription(), is(equalTo("QA Test Description")));
        assertThat(infoPage.getNotes(), is(equalTo("Testing QA notes")));
    }
}
