package com.apriori.evaluate;

import static com.apriori.TestSuiteType.TestSuite.IGNORE;

import com.apriori.cidappapi.entity.response.customizations.Customizations;
import com.apriori.cidappapi.entity.response.customizations.ProcessGroups;
import com.apriori.cidappapi.utils.CustomizationUtil;
import com.apriori.enums.AssemblyProcessGroupEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListProcessGroupTests {

    private SoftAssertions softAssertions;

    @Test
    @TestRail(id = 6197)
    @Description("Get List of Process Groups")
    public void getProcessGroupList() {
        final UserCredentials user = UserUtil.getUser();
        ResponseWrapper<Customizations> customizations = new CustomizationUtil().getCustomizations(user);

        List<String> ignoredProcessGroups = Arrays.asList(
            ProcessGroupEnum.ROLL_UP.getProcessGroup(),
            ProcessGroupEnum.COMPOSITES.getProcessGroup(),
            ProcessGroupEnum.WITHOUT_PG.getProcessGroup(),
            ProcessGroupEnum.RESOURCES.getProcessGroup());

        List<String> processGroupResponse = customizations.getResponseEntity().getItems().stream()
            .map(x -> x.getProcessGroups().stream()
                .map(pg -> pg.getCidSupported() ? pg.getDescription() : null))
            .findAny()
            .orElseThrow(AssertionError::new)
            .collect(Collectors.toList());

        softAssertions = new SoftAssertions();

        ignoredProcessGroups.forEach(ignoredProcessGroup -> softAssertions.assertThat(new ArrayList<>(processGroupResponse)).doesNotContain(ignoredProcessGroup));

        softAssertions.assertAll();
    }

    @Ignore("Assemblies cannot be upload")
    @Test
    @Tag(IGNORE)
    @TestRail(id = 6198)
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

        softAssertions = new SoftAssertions();

        Arrays.stream(AssemblyProcessGroupEnum.getNames()).forEach(assemblyProcessGroup -> softAssertions.assertThat(new ArrayList<>(processGroupResponse)).doesNotContain(assemblyProcessGroup));

        softAssertions.assertAll();
    }
}