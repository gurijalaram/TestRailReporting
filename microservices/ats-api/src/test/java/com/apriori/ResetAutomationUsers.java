package com.apriori;

import com.apriori.ats.entity.request.ResetAutoUsers;
import com.apriori.ats.utils.enums.ATSAPIEnum;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.properties.PropertiesContext;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

// TODO ALL: test it
public class ResetAutomationUsers {
    private static final Logger logger = LoggerFactory.getLogger(ResetAutomationUsers.class);
    private static final String automationPassword = PropertiesContext.get("ats.automation_password");
    private String automationUser;

    /**
     * The initial value in the 'range' is inclusive and upper bound value is exclusive
     */
    @Test
    @Description("Resets the password of the automation users")
    public void resetAllAutomationUsers() {
        String automationUser = PropertiesContext.get("ats.automation_username");

        IntStream.range(1, 41).forEach(x -> {
            String userIndex = (x < 10 ? "0" : "") + x;

            logger.debug(String.format("Resetting password for user 'qa-automation-%s@apriori.com'", userIndex));

            this.automationUser = String.format(automationUser, userIndex);

            RequestEntity requestEntity = RequestEntityUtil.init(ATSAPIEnum.USER_PASSWORD_BY_EMAIL, null)
                .inlineVariables(this.automationUser)
                .urlEncodingEnabled(false)
                .body(ResetAutoUsers.builder()
                    .password(automationPassword)
                    .build())
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

            HTTPRequest.build(requestEntity).patch();
        });
    }
}
