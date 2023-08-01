import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestBaseUI;
import com.apriori.login.CloudHomePage;
import com.apriori.login.LoginService;
import com.apriori.login.UserProfilePage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class UserProfileTests extends TestBaseUI {

    private LoginService aprioriLoginService;
    private UserCredentials currentUser;

    public UserProfileTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        aprioriLoginService = new LoginService(driver, "auth-ui");
    }

    @Test
    @TestRail(id = 17001)
    @Description("Verify that correct input fields are present on User Profile page")
    public void verifyInputFields() {

        List<String> expectedResults = Arrays.asList("Username", "Email", "Given Name", "Family Name", "Name prefix", "Name suffix", "Job title", "Department", "Town or City",
            "County", "Country", "Time Zone", "Office Phone Country Code", "Office Phone Number");
        currentUser = UserUtil.getUser();
        UserProfilePage userProfilePage = aprioriLoginService.login(currentUser, CloudHomePage.class)
            .goToProfilePage();
        assertThat(userProfilePage.getAllInputFieldsName(), is(expectedResults));
    }
}

