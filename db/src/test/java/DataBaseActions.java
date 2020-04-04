import actions.cloud.DbMigration;
import com.apriori.apibase.services.cid.objects.response.ExportSchedulesResponse;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import org.junit.Test;

public class DataBaseActions extends TestUtil {
 @Test
    public void migrateDataFromProfessionalToReportingFailedIfDataIsNotMigrated() {
     ResponseWrapper<ExportSchedulesResponse> response =  DbMigration.migrateFromProToReport();

     this.validateResponseCodeByExpectingAndRealCode(200, response.getStatusCode());
 }
}
