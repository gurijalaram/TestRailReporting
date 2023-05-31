package demo;

import com.apriori.utils.properties.PropertiesContext;
import demo.impl.CommonLoginPageImpl;
import demo.impl.OnPremLoginPageImpl;

public class LoginService {
    private LoginPage loginPage = PropertiesContext.get("env").equals("onprem") ? new OnPremLoginPageImpl() : new CommonLoginPageImpl();

    public void login() {
        loginPage.doLogin();
    }

    public void logout() {
        loginPage.doLogout();
    }
}
