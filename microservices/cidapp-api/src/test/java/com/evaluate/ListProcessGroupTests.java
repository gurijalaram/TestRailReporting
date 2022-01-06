package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cidappapi.entity.response.customizations.Customizations;
import com.apriori.cidappapi.entity.response.customizations.ProcessGroups;
import com.apriori.cidappapi.utils.CustomizationUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterfaces.IgnoreTests;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ListProcessGroupTests {

    private CustomizationUtil customizationUtil;

    @Test
    @TestRail(testCaseId = {"6197"})
    @Description("Get List of Process Groups")
    public void getProcessGroupList() {
        customizationUtil = new CustomizationUtil(new JwtTokenUtil(UserUtil.getUser()).retrieveJwtToken());

        ResponseWrapper<Customizations> customizations = customizationUtil.getCustomizations();

        assertThat(customizations.getResponseEntity().getItems().stream()
            .map(x -> x.getProcessGroups().stream()
                .map(ProcessGroups::getDescription))
            .findAny()
            .orElseThrow(AssertionError::new)
            .collect(Collectors.toList()), hasItems(Arrays.stream(ProcessGroupEnum.getNames())
            .filter(x -> !x.equals(ProcessGroupEnum.ASSEMBLY.getProcessGroup())
                && !x.equals(ProcessGroupEnum.ROLL_UP.getProcessGroup())
                && !x.equals(ProcessGroupEnum.COMPOSITES.getProcessGroup())
                && !x.equals(ProcessGroupEnum.WITHOUT_PG.getProcessGroup())
                && !x.equals(ProcessGroupEnum.RESOURCES.getProcessGroup()))
            .toArray(String[]::new)));
    }

    @Ignore("Assemblies cannot be upload")
    @Test
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"6198"})
    @Description("Get List of Assembly Process Groups")
    public void getAssemblyProcessGroupList() {
//        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
//
//        String componentName = "Piston_assembly";
//        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
//        currentUser = UserUtil.getUser();
//
//        loginPage = new CidAppLoginPage(driver);
//        evaluatePage = loginPage.login(currentUser)
//            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser);
//
//        MatcherAssert.assertThat(evaluatePage.getListOfProcessGroups(), hasItems(AssemblyProcessGroupEnum.getNames()));
    }
}