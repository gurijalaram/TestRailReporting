package com.evaluate.assemblies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class FiltersTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String filterName2 = generateStringUtil.generateFilterName();
    private FilterPage filterPage;
    private UserCredentials currentUser;
    private String assemblyName = "Hinge assembly";
    private final String assemblyExtension = ".SLDASM";
    private List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
    private final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
    private final String componentExtension = ".SLDPRT";

    @Test
    @TestRail(testCaseId = "10538")
    @Description("Verify that filter criteria can be deleted")
    public void filterCriteriaCanBeDeletedTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        filterPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .clear();

        assertTrue(filterPage.isElementDisplayed("No queries applied", "message"));
    }

    @Test
    @TestRail(testCaseId = "10537")
    @Description("Verify that newly created filter is displayed in filters dropdown in my filter section")
    public void newlyCreatedFilterIsDisplayedInFiltersTest() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName2)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .submit(ExplorePage.class);

        assertTrue(explorePage.isElementDisplayed(filterName2, "text-overflow"));
    }

    @Test
    @TestRail(testCaseId = "10535")
    @Description("Verify Cancel button closes the Scenario filter table")
    public void cancelBtnCloseFilterTableTest() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .cancel(ExplorePage.class);

        assertFalse(explorePage.isFilterTablePresent());
    }

    @Test
    @TestRail(testCaseId = "10534")
    @Description("User can clear added criteria simultaneously by Clear button")
    public void canClearAddedCriteriaTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        filterPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Low")
            .deleteAllCriteria();

        assertTrue(filterPage.isElementDisplayed("No queries applied", "message"));
    }

    @Test
    @TestRail(testCaseId = "10532")
    @Description("Validate that user can cancel action New before saving")
    public void canCancelBeforeSavingTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        filterPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .cancel(FilterPage.class);

        assertThat(filterPage.getAllFilters()).doesNotContain(filterName);
    }

    @Test
    @TestRail(testCaseId = "10531")
    @Description("User can filter scenarios from scenario filter modal box")
    public void canFilterScenariosFromModalBoxTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "BIG RING")
            .submit(ExplorePage.class);

        assertThat(explorePage.getAllScenarioComponentName()).containsExactly("BIG RING");
    }

    @Test
    @TestRail(testCaseId = "10529")
    @Description("User can create new filter from already existing one using Save As button")
    public void canCreateNewFilterBySaveAsTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .selectFilter("Missing")
            .saveAs()
            .inputName(filterName)
            .submit(ExplorePage.class);

        assertTrue(explorePage.isElementDisplayed(filterName, "text-overflow"));
    }

    @Test
    @TestRail(testCaseId = "10528")
    @Description("User is able to edit already created filters")
    public void ableToEditCreatedFilterTest() {
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "BIG RING")
            .submit(ExplorePage.class)
            .filterOnTableView()
            .rename()
            .inputName(filterName2)
            .save(FilterPage.class)
            .submit(ExplorePage.class);

        assertTrue(explorePage.isElementDisplayed(filterName2, "text-overflow"));
    }

}
