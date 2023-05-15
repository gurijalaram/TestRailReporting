
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.TestRail;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.login.CloudHomePage;
import com.apriori.utils.login.UserProfilePage;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class UserProfileTests extends TestBase {

    private AprioriLoginPage aprioriLoginPage;
    private UserCredentials currentUser;

    public UserProfileTests() {
        super();
    }

    @Before
    public void setup() {
        aprioriLoginPage = new AprioriLoginPage(driver);
    }

    @Test
    @TestRail(testCaseId = "17001")
    @Description("Verify that correct input fields are present on User Profile page")
    public void verifyInputFields() {

        List<String> expectedResults = Arrays.asList("Username", "Email", "Given Name", "Family Name", "Name prefix", "Name suffix", "Job title", "Department", "Town or City",
            "County", "Country", "Time Zone", "Office Phone Country Code", "Office Phone Number");
        currentUser = UserUtil.getUser();
        UserProfilePage userProfilePage = aprioriLoginPage.login(currentUser, CloudHomePage.class)
            .goToProfilePage();
        assertThat(userProfilePage.getAllInputFieldsName(), is(expectedResults));
    }
}

