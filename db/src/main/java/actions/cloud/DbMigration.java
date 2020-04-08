package actions.cloud;

import com.apriori.apibase.services.cid.objects.request.ExportSchedulesRequest;
import com.apriori.apibase.services.cid.objects.request.SchedulesConfigurationRequest;
import com.apriori.apibase.services.cid.objects.response.ExportSchedulesResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaClearRequest;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http.enums.common.api.CidAdminHttpEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;

import java.util.Collections;

/**
 * Contain data migration functionality between aPriori Professional database <br>
 * and aPriori jasper (reporting) database.
 */
public class DbMigration {

    private static ExportSchedulesResponse latestExportSchedules = null;
    private static UserCredentials migrationUser = UserUtil.getUser();

    /**
     * Migrate data from aPriori Professional database <br>
     * to aPriori jasper (reporting) database.
     *
     * @return ResponseWrapper, more info: <br>
     * @see com.apriori.utils.http.utils.ResponseWrapper
     */
    public static ResponseWrapper<ExportSchedulesResponse> migrateFromProToReport() {
        return new DbMigration().doMigration();
    }

    /**
     * Get the latest ExportSchedules Response, to know migration status and info.
     *
     * @return ExportSchedulesResponse
     */
    public ExportSchedulesResponse getLatestExportSchedules() {
        return latestExportSchedules;
    }

    private ResponseWrapper<ExportSchedulesResponse> doMigration() {
        String schedulesId = this.doInitExportSchedulesAndGetScheduleId();
        this.doStartExportSchedulesByScheduleId(schedulesId);
        return this.doGetSchedulesByScheduleId(schedulesId);
    }

    public String doInitExportSchedulesAndGetScheduleId() {
        return this.doInitExportSchedules().getHeaders().get("x-export-schedule-id").getValue();
    }

    public ResponseWrapper<Object> doInitExportSchedules() {
        RequestEntity requestEntity =
                this.initDefaultRequest(CidAdminHttpEnum.POST_EXPORT_SCHEDULES)
                        .setBody(this.initExportRequestBody())
                        .setStatusCode(HttpStatus.SC_CREATED);

        return GenericRequestUtil.post(requestEntity, new RequestAreaClearRequest());
    }

    public ResponseWrapper<Object> doStartExportSchedulesByScheduleId(String schedulesId) {
        RequestEntity requestEntity =
                this.initDefaultRequest(CidAdminHttpEnum.POST_EXPORT_NOW_BY_SCHEDULE_ID)
                        .setInlineVariables(schedulesId)
                        .setStatusCode(HttpStatus.SC_NO_CONTENT);

        return GenericRequestUtil.post(requestEntity, new RequestAreaClearRequest());
    }

    public ResponseWrapper<ExportSchedulesResponse> doGetSchedulesByScheduleId(final String schedulesId) {
        RequestEntity requestEntity =
                this.initDefaultRequest(CidAdminHttpEnum.GET_EXPORT_JOB_BY_SCHEDULE_ID)
                        .setInlineVariables(schedulesId)
                        .setReturnType(ExportSchedulesResponse.class);

        ResponseWrapper<ExportSchedulesResponse> exportSchedulesResponseWrapper =
                GenericRequestUtil.get(requestEntity, new RequestAreaClearRequest());

        latestExportSchedules = exportSchedulesResponseWrapper.getResponseEntity();

        return exportSchedulesResponseWrapper;
    }

    public ResponseWrapper<Object> doDeleteExportSchedulesBySchedulesId(String schedulesId) {
        RequestEntity requestEntity =
                this.initDefaultRequest(CidAdminHttpEnum.DELETE_EXPORT_BY_SCHEDULE_ID)
                        .setInlineVariables(schedulesId)
                        .setStatusCode(HttpStatus.SC_NO_CONTENT);

        return GenericRequestUtil.delete(requestEntity, new RequestAreaClearRequest());
    }

    private ExportSchedulesRequest initExportRequestBody() {
        return new ExportSchedulesRequest()
                .setType("exportSetRequestBean")
                .setDisabled(false)
                .setName("Automation TEST Scenarios " + RandomStringUtils.randomAlphabetic(6))
                .setSchedules(
                        Collections.singletonList(new SchedulesConfigurationRequest()
                                .setType("SIMPLE")
                                .setRepeatCount(0)
                                .setRepeatInterval(0))
                )
                .setDescription("Test export from auth")
                .setExportDynamicRollups(false)
                .setExportScope("All Delta")
                .setSourceSchema("Default Scenarios");
    }

    private RequestEntity initDefaultRequest(final EndpointEnum endpoint) {
        return RequestEntity.init(endpoint,
                //TODO z: should be uncommented when aoth0 and jasper server will have the same test users credentials
                //migrationUser
                UserCredentials.init("qa-automation-01@apriori.com", "qa-automation-01"),
                null
        ).setAutoLogin(true);
    }
}
