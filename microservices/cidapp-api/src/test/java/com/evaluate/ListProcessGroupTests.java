package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

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

import java.util.Arrays;
import java.util.stream.Collectors;

public class ListProcessGroupTests {

    private CustomizationUtil customizationUtil;

    @Test
    @TestRail(testCaseId = {"6197"})
    @Description("Get List of Process Groups")
    public void getProcessGroupList() {
        final UserCredentials user = UserUtil.getUser();
        ResponseWrapper<Customizations> customizations = customizationUtil.getCustomizations(user);

        assertThat(customizations.getResponseEntity().getItems().stream()
            .map(x -> x.getProcessGroups().stream()
                .map(ProcessGroups::getDescription))
            .findAny()
            .orElseThrow(AssertionError::new)
            .collect(Collectors.toList()), hasItems(Arrays.stream(ProcessGroupEnum.getReducedProcessGroup()).toArray()));
    }

    @Ignore("Assemblies cannot be upload")
    @Test
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"6198"})
    @Description("Get List of Assembly Process Groups")
    public void getAssemblyProcessGroupList() {
        final UserCredentials user = UserUtil.getUser();
        ResponseWrapper<Customizations> customizations = customizationUtil.getCustomizations(user);

        assertThat(customizations.getResponseEntity().getItems().stream()
            .map(x -> x.getProcessGroups().stream()
                .map(ProcessGroups::getDescription))
            .findAny()
            .orElseThrow(AssertionError::new)
            .collect(Collectors.toList()), hasItems(Arrays.stream(AssemblyProcessGroupEnum.getNames()).toArray()));
    }
}