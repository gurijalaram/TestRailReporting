import com.apriori.utils.web.driver.TestBase;
import org.junit.Test;

public class LoginTests extends TestBase {

    public static final String cicQA = "cic.qa.awsdev.apriori.com/Thingworx/Runtime/index.html#master=PLMC_MasterMashup_Master&mashup=PLMC_JobManagement_MU";

    public LoginTests() {
        super();
    }

    public void login(String uname, String pwd){
        String URL = "https://" + uname + ":" + pwd + "@" + cicQA;
        driver.get(URL);
    }

    @Test
    public void testLogin(){
        login("kpatel@apriori.com", "CostInsight2019");
    }
}
