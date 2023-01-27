package tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.utils.AuthorizeUserUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.authorization.Token;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.Test;

public class AtsAuthorization extends TestUtil {

    @Test
    @TestRail(testCaseId = {"3581"})
    @Description("Generate a JWT from the ATS Token endpoint")
    public void generateTokenTest() {
        ResponseWrapper<Token> response = new AuthorizationUtil().getToken();

        assertThat(response.getResponseEntity().getToken(), is(not(emptyString())));
    }

    @Test
    @TestRail(testCaseId = {"3913"})
    @Description("Authorize a user to access a specified application")
    public void authorizeUserTest() {
        final UserCredentials userCredentials = UserUtil.getUser();

        ResponseWrapper<AuthorizationResponse> response = AuthorizeUserUtil.authorizeUser(userCredentials.generateCloudContext().getCloudContext(), userCredentials.getToken());

        assertThat(response.getResponseEntity().getEmail(), is(equalTo(userCredentials.getEmail())));
    }
}
