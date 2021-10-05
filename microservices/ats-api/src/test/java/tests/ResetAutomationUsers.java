package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.ats.entity.request.ResetAutoUsers;
import com.apriori.ats.utils.enums.ATSAPIEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

// TODO ALL: test it
public class ResetAutomationUsers {
    private static final Logger logger = LoggerFactory.getLogger(ResetAutomationUsers.class);
    private static final String automationPassword = PropertiesContext.get("${env}.ats.automation_password");
    private static String automationUser = PropertiesContext.get("${env}.ats.automation_username");

    /**
     * The initial value in the 'range' is inclusive and upper bound value is exclusive
     */
    @Test
    @Description("Resets the password of the automation users")
    public void resetAllAutomationUsers() {
        IntStream.range(1, 41).forEach(x -> {
            String userIndex = (x < 10 ? "0" : "") + x;

            logger.debug(String.format("Resetting password for user 'qa-automation-%s@apriori.com'", userIndex));

            automationUser = String.format(automationUser, userIndex);

            RequestEntity requestEntity = RequestEntityUtil.init(ATSAPIEnum.PATCH_USER_PASSWORD_BY_USERNAME, null)
                .inlineVariables(automationUser)
                .urlEncodingEnabled(false)
                .body(new ResetAutoUsers().setPassword(automationPassword));

            ResponseWrapper<String> resetAutoUsersResponse = HTTPRequest.build(requestEntity).patch();

            assertThat(resetAutoUsersResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
        });
    }
}
