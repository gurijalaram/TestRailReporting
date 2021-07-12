import com.apriori.utils.properties.PropertiesContext;
import org.junit.Test;

public class PropertiesTest {

    @Test
    public void testProps() {
        String value = PropertiesContext.getStr("global.base_url");
        Integer integer = PropertiesContext.getInt("global.int");
        String env = PropertiesContext.getStr("${env}.current_env");
        String envGlobal = PropertiesContext.getStr("env");

        System.out.println(value);
        System.out.println(integer);
        System.out.println(env);
        System.out.println(envGlobal);
    }

}
