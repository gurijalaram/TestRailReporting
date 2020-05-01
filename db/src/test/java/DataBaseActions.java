import com.apriori.apibase.services.cid.objects.response.ExportSchedulesResponse;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.database.actions.cloud.DbMigration;
import com.apriori.database.entity.MigrationEntity;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.http.utils.ResponseWrapper;

import com.fbc.datamodel.shared.ScenarioType;
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
     * To migrate specific scenario from aPriori Professional database to jasper (reporting) database <br>
     */
    @Test
    public void jenkinsMigrationOfSpecificScenarioFromProfessionalToReportingFailedIfDataIsNotMigrated() {

        System.out.println("*********************" + System.getProperty("JENKINS_TEST"));

//        ResponseWrapper<ExportSchedulesResponse> response = DbMigration.migrateSpecificScenario(
//                MigrationEntity.initWithNewScenarioNameForMigration(ScenarioType.valueOf(Constants.SCENARIO_TYPE),
//                        Constants.ELEMENT_NAME,
//                        Constants.SCENARIO_NAME,
//                        Constants.NEW_SCENARIO_NAME
//                )
//        );
//
//        this.validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    /**
     * To migrate specific scenario from aPriori Professional database to jasper (reporting) database <br>
     * run this command in cmd from build folder: <br>
     * gradle clean -Denv=<env name> :database:test --tests "DataBaseActions.migrateSpecificScenarioFromProfessionalToReportingFailedIfDataIsNotMigrated"
     */
    @Test
    public void migrateSpecificScenarioFromProfessionalToReportingFailedIfDataIsNotMigrated() {
        ResponseWrapper<ExportSchedulesResponse> response = DbMigration.migrateSpecificScenario(MigrationEntity.initWithNewScenarioNameForMigration(ScenarioType.ASSEMBLY,
                "PISTON_ASSEMBLY",
                "Initial", "12234")
        );

        this.validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    /**
     * To migrate data from aPriori Professional database to jasper (reporting) database <br>
     * run this command in cmd from build folder: <br>
     * gradle clean -Denv=<env name> :database:test --tests "DataBaseActions.migrateDataFromProfessionalToReportingFailedIfDataIsNotMigrated"
     */
    @Test
    public void migrateDataFromProfessionalToReportingFailedIfDataIsNotMigrated() {
        ResponseWrapper<ExportSchedulesResponse> response = DbMigration.migrateFromProToReport();

        this.validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    public void getExportSchedulesFailedIfSchedulesWasNotReceived() {
        final String scheduleId = dbMigration.initExportSchedulesAndGetScheduleId();
        final ResponseWrapper<ExportSchedulesResponse> response = dbMigration.doGetSchedulesByScheduleId(scheduleId);
        this.validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());

        removeExportSchedule(dbMigration, scheduleId);
    }

    @Test
    public void createExportSchedulesFailedIfSchedulesWasNotCreated() {
        Header scheduleIdHeader = dbMigration.doCreateExportSchedules(dbMigration.initExportRequestBody()).getHeaders().get("x-export-schedule-id");

        Assert.assertNotNull("After creating export schedule, scheduleId should be generated.", scheduleIdHeader);

        removeExportSchedule(dbMigration, scheduleIdHeader.getValue());
    }

    private void removeExportSchedule(final DbMigration dbMigration, final String scheduleId) {
        dbMigration.doDeleteExportSchedulesBySchedulesId(scheduleId);
    }
}
