package com.apriori.cid.api.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.IGNORE;

import com.apriori.cid.api.models.response.customizations.Customizations;
import com.apriori.cid.api.models.response.customizations.ProcessGroups;
import com.apriori.cid.api.utils.CustomizationUtil;
import com.apriori.shared.util.enums.AssemblyProcessGroupEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
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

    @Disabled("Assemblies cannot be upload")
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