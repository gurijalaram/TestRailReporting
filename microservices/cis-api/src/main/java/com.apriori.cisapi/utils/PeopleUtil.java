package com.apriori.cisapi.utils;

import com.apriori.cisapi.entity.enums.CisAPIEnum;
import com.apriori.cisapi.entity.response.PeopleResponse;
import com.apriori.cisapi.entity.response.PersonResponse;
import com.apriori.cisapi.entity.response.User;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

public class PeopleUtil {

    /**
     * GET current user
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    public User getCurrentUser(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.CURRENT_USER, User.class)
            .token(userCredentials.getToken());

        ResponseWrapper<User> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse.getResponseEntity();
    }

    /**
     * GET current person
     *
     * @param userCredentials - the user credentials
     * @return person object
     */
    public PersonResponse getCurrentPerson(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.CURRENT_PERSON, PeopleResponse.class)
            .token(userCredentials.getToken())
            .inlineVariables(userCredentials.getUsername());

        ResponseWrapper<PeopleResponse> peopleResponse = HTTPRequest.build(requestEntity).get();
        return peopleResponse.getResponseEntity().getItems().get(0);
    }
}
