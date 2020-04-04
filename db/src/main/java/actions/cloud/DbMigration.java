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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;

import java.util.Collections;

public class DbMigration {

    public static ResponseWrapper<ExportSchedulesResponse> migrateFromProToReport() {
        return new DbMigration().doMigration();
    }

    private ResponseWrapper<ExportSchedulesResponse> doMigration() {
        String schedulesId = this.doInitExportSchedulesAndGetSchedulesId();
        this.doStartExportSchedulesBySchedulesId(schedulesId);
        return this.doGetSchedules(schedulesId);
    }

    public void doStartExportSchedulesBySchedulesId(String schedulesId) {
        RequestEntity requestEntity =
                this.initDefaultRequest(CidAdminHttpEnum.POST_EXPORT_NOW)
                        .setInlineVariables(schedulesId)
                .setStatusCode(HttpStatus.SC_CREATED);

        GenericRequestUtil.post(requestEntity, new RequestAreaClearRequest());
    }

    public ResponseWrapper<ExportSchedulesResponse> doGetSchedules(final String schedulesId) {
        RequestEntity requestEntity =
                this.initDefaultRequest(CidAdminHttpEnum.GET_EXPORT_JOBS)
                        .setInlineVariables(schedulesId)
                .setReturnType(ExportSchedulesResponse.class);

        return GenericRequestUtil.get(requestEntity, new RequestAreaClearRequest());
    }

    public String doInitExportSchedulesAndGetSchedulesId() {
        RequestEntity requestEntity =
                this.initDefaultRequest(CidAdminHttpEnum.POST_EXPORT_SCHEDULES).setBody(this.initExportRequestBody());

        return GenericRequestUtil.post(requestEntity, new RequestAreaClearRequest()).getHeaders().get("x-export-schedule-id").getValue();
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
                UserCredentials.init("qa-automation-01@apriori.com", "qa-automation-01"),
                null
        ).setAutoLogin(true);
    }

}
