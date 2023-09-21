package com.apriori.database.actions.cloud;

/**
 * Contain data migration functionality between aPriori Professional database <br>
 * and aPriori jasper (reporting) database.
 */
public class DbMigration {

    // TODO Nexus issue: should be updated with other approach

//    private static final String exportScheduleIdKey = "x-export-schedule-id";
//
//    private static ExportSchedulesResponse latestExportSchedules = null;
//    private static UserCredentials migrationUser = UserUtil.getUser();
//
//    /**
//     * Migrate data from aPriori Professional database <br>
//     * to aPriori jasper (reporting) database.
//     *
//     * @return ResponseWrapper, more info: <br>
//     * @see com.apriori.utils.http.utils.ResponseWrapper
//     */
//    public static ResponseWrapper<ExportSchedulesResponse> migrateFromProToReport() {
//        DbMigration dbMigration = new DbMigration();
//
//        return dbMigration.doMigrationByScheduleId(
//                dbMigration.initExportSchedulesAndGetScheduleId());
//    }
//
//    /**
//     * Migrate specific scenario with (part, assembly rollup) from aPriori Professional database <br>
//     * to aPriori jasper (reporting) database.
//     *
//     * @return ResponseWrapper, more info: <br>
//     * @see com.apriori.utils.http.utils.ResponseWrapper
//     */
//    public static ResponseWrapper<ExportSchedulesResponse> migrateSpecificScenario(final MigrationEntity migrationEntity) {
//        DbMigration dbMigration = new DbMigration();
//
//        return dbMigration.doMigrationByScheduleId(
//                dbMigration.initSpecificExportSchedulesAndGetScheduleId(migrationEntity));
//    }
//
//    /**
//     * Get the latest ExportSchedules Response, to know migration status and info.
//     *
//     * @return ExportSchedulesResponse
//     */
//    public ExportSchedulesResponse getLatestExportSchedules() {
//        return latestExportSchedules;
//    }
//
//    private ResponseWrapper<ExportSchedulesResponse> doMigrationByScheduleId(String scheduleId) {
//        this.doStartExportSchedulesByScheduleId(scheduleId);
//        return this.doGetSchedulesByScheduleId(scheduleId);
//    }
//
//    public String initSpecificExportSchedulesAndGetScheduleId(final MigrationEntity migrationEntity) {
//        return this.doCreateExportSchedules(
//                this.initSpecificExportRequestBody(migrationEntity)
//        ).getHeaders().get(exportScheduleIdKey).getValue();
//    }
//
//    public String initExportSchedulesAndGetScheduleId() {
//        return this.doCreateExportSchedules(
//                this.initExportRequestBody()
//        ).getHeaders().get(exportScheduleIdKey).getValue();
//    }
//
//    public ResponseWrapper<Object> doCreateExportSchedules(Object body) {
//        RequestEntity requestEntity =
//                this.initDefaultRequest(CidAdminHttpEnum.POST_EXPORT_SCHEDULES)
//                        .setBody(body);
//
//        return GenericRequestUtil.post(requestEntity, new RequestAreaClearRequest());
//    }
//
//    public ResponseWrapper<Object> doStartExportSchedulesByScheduleId(String schedulesId) {
//        RequestEntity requestEntity =
//                this.initDefaultRequest(CidAdminHttpEnum.POST_EXPORT_NOW_BY_SCHEDULE_ID)
//                        .setInlineVariables(schedulesId)
//                        .setStatusCode(HttpStatus.SC_NO_CONTENT);
//
//        return GenericRequestUtil.post(requestEntity, new RequestAreaClearRequest());
//    }
//
//    public ResponseWrapper<ExportSchedulesResponse> doGetSchedulesByScheduleId(final String schedulesId) {
//        RequestEntity requestEntity =
//                this.initDefaultRequest(CidAdminHttpEnum.GET_EXPORT_JOB_BY_SCHEDULE_ID)
//                        .setInlineVariables(schedulesId)
//                        .setReturnType(ExportSchedulesResponse.class);
//
//        ResponseWrapper<ExportSchedulesResponse> exportSchedulesResponseWrapper =
//                GenericRequestUtil.get(requestEntity, new RequestAreaClearRequest());
//
//        latestExportSchedules = exportSchedulesResponseWrapper.getResponseEntity();
//
//        return exportSchedulesResponseWrapper;
//    }
//
//    public ResponseWrapper<Object> doDeleteExportSchedulesBySchedulesId(String schedulesId) {
//        RequestEntity requestEntity =
//                this.initDefaultRequest(CidAdminHttpEnum.DELETE_EXPORT_BY_SCHEDULE_ID)
//                        .setInlineVariables(schedulesId)
//                        .setStatusCode(HttpStatus.SC_NO_CONTENT);
//
//        return GenericRequestUtil.delete(requestEntity, new RequestAreaClearRequest());
//    }
//
//    public SpecificExportSchedulesRequest initSpecificExportRequestBody(final MigrationEntity migrationEntity) {
//        SpecificExportSchedulesRequest specificESR = new SpecificExportSchedulesRequest();
//        specificESR.setType("exportSetRequestBean");
//        specificESR.setDisabled(false);
//        specificESR.setName(migrationEntity.getNewScenarioName());
//        specificESR.setDescription("Specific export from auth");
//        specificESR.setExportDynamicRollups(false);
//        specificESR.setExportScope("All Delta");
//        specificESR.setSourceSchema("Default Scenarios");
//        specificESR.setSchedules(
//                Collections.singletonList(new SchedulesConfigurationRequest()
//                        .setType("SIMPLE")
//                        .setRepeatCount(0)
//                        .setRepeatInterval(0))
//        );
//        specificESR.setScenarioKeyRequest(
//                new ScenarioKeyRequest().setTypeName(migrationEntity.getScenarioType().baseName)
//                        .setMasterName(migrationEntity.getElementName())
//                        .setStateName(migrationEntity.getScenarioName())
//        );
//
//        return specificESR;
//    }
//
//    public ExportSchedulesRequest initExportRequestBody() {
//        return new ExportSchedulesRequest()
//                .setType("exportSetRequestBean")
//                .setDisabled(false)
//                .setName("Automation TEST Scenarios " + RandomStringUtils.randomAlphabetic(6))
//                .setSchedules(
//                        Collections.singletonList(new SchedulesConfigurationRequest()
//                                .setType("SIMPLE")
//                                .setRepeatCount(0)
//                                .setRepeatInterval(0))
//                )
//                .setDescription("Test export from auth")
//                .setExportDynamicRollups(false)
//                .setExportScope("All Delta")
//                .setSourceSchema("Default Scenarios");
//    }
//
//    private RequestEntity initDefaultRequest(final EndpointEnum endpoint) {
//
//        UserCredentials userCredentials;
//
//        if (Constants.PROP_USER_NAME != null && Constants.PROP_USER_PASSWORD != null) {
//            userCredentials = UserCredentials.init(Constants.PROP_USER_NAME, Constants.PROP_USER_PASSWORD);
//        } else {
//            //TODO: should be uncommented when auth0 and jasper server will have the same test users credentials
//            //migrationUser
//            userCredentials = UserCredentials.init("qa-automation-01@apriori.com", "qa-automation-01");
//        }
//
//        return RequestEntity.init(endpoint,
//                userCredentials,
//                null
//        ).setAutoLogin(true);
//    }
}
