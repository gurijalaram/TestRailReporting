import actions.cloud.DbMigration;
import com.apriori.apibase.services.cid.objects.response.ExportSchedulesResponse;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import io.restassured.http.Header;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DataBaseActions extends TestUtil {

    private DbMigration dbMigration;

    @Before
    public void createNewDbMigrationInstance() {
       dbMigration = new DbMigration();
    }

    /**
     * To migrate data from aPriori Professional database to jasper (reporting) database <br>
     *     run this command in cmd from build folder: <br>
     *         gradle clean -Denv=<env name> :database:test --tests "DataBaseActions.migrateDataFromProfessionalToReportingFailedIfDataIsNotMigrated"
     */
    @Test
    public void migrateDataFromProfessionalToReportingFailedIfDataIsNotMigrated() {
        ResponseWrapper<ExportSchedulesResponse> response = DbMigration.migrateFromProToReport();

        this.validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    public void getExportSchedulesFailedIfSchedulesWasNotReceived() {
        final String scheduleId = dbMigration.doInitExportSchedulesAndGetScheduleId();
        final ResponseWrapper<ExportSchedulesResponse> response = dbMigration.doGetSchedulesByScheduleId(scheduleId);
        this.validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());

        removeExportSchedule(dbMigration, scheduleId);
    }

    @Test
    public void createExportSchedulesFailedIfSchedulesWasNotCreated() {
        Header scheduleIdHeader = dbMigration.doInitExportSchedules().getHeaders().get("x-export-schedule-id");

        Assert.assertNotNull("After creating export schedule, scheduleId should be generated.", scheduleIdHeader);

        removeExportSchedule(dbMigration, scheduleIdHeader.getValue());
    }

    private void removeExportSchedule(final DbMigration dbMigration, final String scheduleId) {
            dbMigration.doDeleteExportSchedulesBySchedulesId(scheduleId);
    }
}
