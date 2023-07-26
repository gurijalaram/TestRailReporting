package tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.apriori.TestUtil;
import com.apriori.ats.entity.response.AuthorizationResponse;
import com.apriori.ats.entity.response.CloudContextResponse;
import com.apriori.ats.utils.AtsTestUtil;
import com.apriori.ats.utils.AuthorizeUserUtil;
import com.apriori.ats.utils.enums.ATSAPIEnum;
import com.apriori.authorization.AuthorizationUtil;
import com.apriori.authorization.response.Token;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class AtsAuthorization extends TestUtil {
    final UserCredentials userCredentials = UserUtil.getUser();
    private AtsTestUtil atsTestUtil = new AtsTestUtil();
    SoftAssertions soft = new SoftAssertions();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Test
    @TestRail(id = {3581})
    @Description("Generate a JWT from the ATS Token endpoint")
    public void generateTokenTest() {
        ResponseWrapper<Token> response = new AuthorizationUtil().getToken();

        assertThat(response.getResponseEntity().getToken(), is(not(emptyString())));
    }

    @Test
    @TestRail(id = {3913})
    @Description("Authorize a user to access a specified application")
    public void authorizeUserTest() {

        ResponseWrapper<AuthorizationResponse> response = AuthorizeUserUtil.authorizeUser(userCredentials.generateCloudContext().getCloudContext(), userCredentials.getToken());

        assertThat(response.getResponseEntity().getEmail(), is(equalTo(userCredentials.getEmail())));
    }

    @Test
    @TestRail(id = {22088})
    @Description("Get a Cloud Context identified by a cloud context string")
    public void getCloudContext() {
        String customerIdentity = cdsTestUtil.getAprioriInternal().getIdentity();
        String contextString = userCredentials.generateCloudContext().getCloudContext();
        ResponseWrapper<CloudContextResponse> getCloudContext = atsTestUtil.getCommonRequest(ATSAPIEnum.CLOUD_CONTEXT, CloudContextResponse.class, HttpStatus.SC_OK, contextString);

        soft.assertThat(getCloudContext.getResponseEntity().getCustomerIdentity()).isEqualTo(customerIdentity);
        soft.assertAll();
    }
}
