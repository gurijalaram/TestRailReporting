package tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.utils.AuthorizeUserUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.token.Token;
import com.apriori.utils.token.TokenUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class AtsAuthorization extends TestUtil {

    @Test
    @TestRail(testCaseId = {"3581"})
    @Description("Retrieve a JWT from the ATS Token endpoint")
    public void getToken() {
        ResponseWrapper<Token> response = new TokenUtil().getToken();

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }

    @Test
    @TestRail(testCaseId = {"3913"})
    @Description("Authorize a user to access a specified application")
    public void authorizeUser() {
        ResponseWrapper<Token> token = new TokenUtil().getToken();

        ResponseWrapper<AuthorizationResponse> response = AuthorizeUserUtil.authorizeUser(
            PropertiesContext.get("${env}.auth_target_cloud_context"), token.getResponseEntity().getToken());

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
