import com.apriori.utils.web.driver.TestBase;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginTests extends TestBase {

    public static final String cicQA = "https://cic.qa.awsdev.apriori.com/Thingworx/Runtime/index.html#master=PLMC_MasterMashup_Master&mashup=PLMC_JobManagement_MU";

    public LoginTests() {
        super();
    }

    public void login(String uname, String pwd){
        String URL = "http://" + uname + ":" + pwd + "@" + cicQA;
        driver.get(URL);
    }

    @Test
    public void testLogin(){
        driver = new ChromeDriver();
        login("kpatel@apriori.com", "CostInsight2019");
    }
}
