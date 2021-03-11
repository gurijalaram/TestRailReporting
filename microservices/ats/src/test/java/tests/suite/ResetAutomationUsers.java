package tests.suite;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.ats.entity.request.ResetAutoUsers;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class ResetAutomationUsers {

    private static final Logger logger = LoggerFactory.getLogger(ResetAutomationUsers.class);

    String url = "https://ats.na-1.qa-20-1.apriori.net/users/qa-automation-%s@apriori.com/password?key=X0V4XXK2SF87";
    String password = "TrumpetSnakeFridgeToasty18!%";

    @Test
    public void resetAllUsers() {
        IntStream.range(1, 41).forEach(x -> {
            String autoIndex = (x < 10 ? "0" : "") + x;

            logger.debug(String.format("Resetting password for user 'qa-automation-%s@apriori.com'", autoIndex));

            RequestEntity requestEntity = RequestEntity.init(String.format(url, autoIndex), null)
                .setUrlEncodingEnabled(false)
                .setBody(new ResetAutoUsers().setPassword(password));

            ResponseWrapper<String> resetAutoUsersResponse = GenericRequestUtil.patch(requestEntity, new RequestAreaApi());

            assertThat(resetAutoUsersResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
        });
    }
}
