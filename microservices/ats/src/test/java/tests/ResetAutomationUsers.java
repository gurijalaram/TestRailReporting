package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.ats.entity.request.ResetAutoUsers;
import com.apriori.ats.utils.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class ResetAutomationUsers {

    private static final Logger logger = LoggerFactory.getLogger(ResetAutomationUsers.class);

    String url = String.format(
            "https://%s/users/%s/password?key=%s",
            Constants.getAtsServiceHost(),
            Constants.getAutomationUsername(),
            Constants.getSecretKey()
    );

    /**
     * The initial value in the 'range' is inclusive and upper bound value is exclusive
     */
    @Test
    @Description("Resets the password of the automation users")
    public void resetAllAutomationUsers() {
        IntStream.range(1, 41).forEach(x -> {
            String userIndex = (x < 10 ? "0" : "") + x;

            logger.debug(String.format("Resetting password for user 'qa-automation-%s@apriori.com'", userIndex));

            RequestEntity requestEntity = RequestEntity.init(String.format(url, userIndex), null)
                .setUrlEncodingEnabled(false)
                .setBody(new ResetAutoUsers().setPassword(Constants.getAutomationPassword()));

            ResponseWrapper<String> resetAutoUsersResponse = GenericRequestUtil.patch(requestEntity, new RequestAreaApi());

            assertThat(resetAutoUsersResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
        });
    }
}
