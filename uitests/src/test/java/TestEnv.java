import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserDataUtil;

import org.junit.Test;

public class TestEnv {
    @Test
    public void FailedIf(){
        System.setProperty(Constants.defaultEnvironmentKey, "cid-te");
        System.out.println("*************: " + Constants.url);
        System.out.println("*************: " + Constants.usersFile);

        System.out.println("USERS: " + UserDataUtil.getUser().getUsername() + "              " + UserDataUtil.getUser().getPassword());
        System.out.println("USERS: " + UserDataUtil.getUser().getUsername() + "              " + UserDataUtil.getUser().getPassword());
        System.out.println("USERS: " + UserDataUtil.getUser().getUsername() + "              " + UserDataUtil.getUser().getPassword());
        System.out.println("USERS: " + UserDataUtil.getUser().getUsername() + "              " + UserDataUtil.getUser().getPassword());
        System.out.println("USERS: " + UserDataUtil.getUser().getUsername() + "              " + UserDataUtil.getUser().getPassword());
        System.out.println("USERS: " + UserDataUtil.getUser().getUsername() + "              " + UserDataUtil.getUser().getPassword());
    }
}
