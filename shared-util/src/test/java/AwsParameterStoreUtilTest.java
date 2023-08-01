import com.apriori.AwsParameterStoreUtil;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class AwsParameterStoreUtilTest {

    @Test
    public void testGetSystemParameter() {
        final String apiId = AwsParameterStoreUtil.getSystemParameter("/dev-cis-sbx-apint/cds/api_id");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(apiId).isNotEmpty();
        softAssertions.assertAll();
    }
}
