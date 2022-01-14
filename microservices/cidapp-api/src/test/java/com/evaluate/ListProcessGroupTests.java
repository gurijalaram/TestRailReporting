package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import com.apriori.cidappapi.entity.response.customizations.Customizations;
import com.apriori.cidappapi.entity.response.customizations.ProcessGroups;
import com.apriori.cidappapi.utils.CustomizationUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.AssemblyProcessGroupEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterfaces.IgnoreTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListProcessGroupTests {

    @Test
    @TestRail(testCaseId = {"6197"})
    @Description("Get List of Process Groups")
    public void getProcessGroupList() {
        final UserCredentials user = UserUtil.getUser();
        ResponseWrapper<Customizations> customizations = new CustomizationUtil().getCustomizations(user);

        List<String> ignoredProcessGroups = Arrays.asList(
            ProcessGroupEnum.ASSEMBLY.getProcessGroup(),
            ProcessGroupEnum.ROLL_UP.getProcessGroup(),
            ProcessGroupEnum.COMPOSITES.getProcessGroup(),
            ProcessGroupEnum.WITHOUT_PG.getProcessGroup(),
            ProcessGroupEnum.RESOURCES.getProcessGroup());

        List<String> processGroupResponse = customizations.getResponseEntity().getItems().stream()
            .map(x -> x.getProcessGroups().stream()
                .map(ProcessGroups::getDescription))
            .findAny()
            .orElseThrow(AssertionError::new)
            .collect(Collectors.toList());

        ignoredProcessGroups.forEach(ignoredProcessGroup -> assertThat(new ArrayList<>(processGroupResponse), not(hasItem(ignoredProcessGroup))));
    }

    @Ignore("Assemblies cannot be upload")
    @Test
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"6198"})
    @Description("Get List of Assembly Process Groups")
    public void getAssemblyProcessGroupList() {
        final UserCredentials user = UserUtil.getUser();
        ResponseWrapper<Customizations> customizations = new CustomizationUtil().getCustomizations(user);

        List<String> processGroupResponse = customizations.getResponseEntity().getItems().stream()
            .map(x -> x.getProcessGroups().stream()
                .map(ProcessGroups::getDescription))
            .findAny()
            .orElseThrow(AssertionError::new)
            .collect(Collectors.toList());

        Arrays.stream(AssemblyProcessGroupEnum.getNames()).forEach(assemblyProcessGroup -> assertThat(new ArrayList<>(processGroupResponse), not(hasItem(assemblyProcessGroup))));
    }
}