package com.apriori.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.Users;
import com.apriori.cds.utils.CdsTestUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserCreation extends TestUtil {

    private CdsTestUtil cdsTestUtil;
    private Customer aprioriInternal;
    private List<User> sourceUsers;
    private static final String STAFF_TEST_USER = "staff-test-user";

    /**
     * Creates customer users
     *
     * @param count - number of users
     * @param customerIdentity - identity of customer
     * @param customerName - customer name
     */
    public void populateStaffTestUsers(int count, String customerIdentity, String customerName) {
        Map<String, Object> existingUsers = Collections.singletonMap("username[CN]", STAFF_TEST_USER);
        cdsTestUtil = new CdsTestUtil();
        aprioriInternal = cdsTestUtil.getAprioriInternal();
        sourceUsers = new ArrayList<>(cdsTestUtil.findAll(
                CDSAPIEnum.GET_USERS_BY_CUSTOMER_ID,
                Users.class,
                existingUsers,
                Collections.emptyMap(),
                aprioriInternal.getIdentity()
        ));

        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        for (int i = sourceUsers.size(); i < count; ++i) {
            String username = String.format("%s-%s-%s", STAFF_TEST_USER, now, i);
            User added = cdsTestUtil.addUser(customerIdentity, username, customerName).getResponseEntity();
            sourceUsers.add(added);
        }
    }
}