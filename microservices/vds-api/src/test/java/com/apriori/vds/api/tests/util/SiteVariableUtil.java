package com.apriori.vds.api.tests.util;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariable;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariablesItems;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;

public class SiteVariableUtil extends TestUtil {
    protected static final String updatedName = new GenerateStringUtil().generateAlphabeticString("Site", 5);
    protected static final String updatedType = "STRING";
    protected static final String updatedValue = "UpdatedValue";
    protected static final String updatedNotes = "UpdatedNotes";
    private RequestEntityUtil requestEntityUtil;

    public SiteVariableUtil(RequestEntityUtil requestEntityUtil) {
        super.requestEntityUtil = requestEntityUtil;
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * Calls an API with DELETE verb
     *
     * @param name - the name
     */
    public void deleteSiteVariables(final String name) {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.SITE_VARIABLE_BY_ID, null)
                .inlineVariables(name)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Calls an API with PUT verb
     *
     * @param siteVariableRequest  - the site variable request
     * @param expectedResponseCode - the expected response code
     * @param klass                - the class
     * @param <T>                  - the generic object
     * @return generic object
     */
    public <T> ResponseWrapper<T> putSiteVariables(SiteVariableRequest siteVariableRequest, Integer expectedResponseCode, Class<T> klass) {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.SITE_VARIABLES, klass)
                .body(siteVariableRequest)
                .expectedResponseCode(expectedResponseCode);

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * Calls an API with PUT verb
     *
     * @param processGroupId           - the process group id
     * @param siteVariableBeforeUpdate - the site variable before update
     * @return new object
     */
    public SiteVariable putSiteVariable(String processGroupId, SiteVariable siteVariableBeforeUpdate) {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PROCESS_GROUP_SITE_VARIABLE_BY_PG_ID, SiteVariable.class)
                .inlineVariables(processGroupId)
                .body(initUpdateRequestBody(siteVariableBeforeUpdate))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();

        return updatedSiteVariableResponse.getResponseEntity();
    }

    /**
     * Calls an API with delete verb
     *
     * @param name - the name
     */
    public void deleteProcessGroupSiteVariableByName(final String name) {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_ID, null)
                .inlineVariables(new ProcessGroupUtil(requestEntityUtil).getProcessGroupIdentity(), name)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Calls an API with PUT verb
     *
     * @return new object
     */
    public SiteVariable putProcessGroupSiteVariables() {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PROCESS_GROUP_SITE_VARIABLE_BY_PG_ID, SiteVariable.class)
                .inlineVariables(new ProcessGroupUtil(requestEntityUtil).getProcessGroupIdentity())
                .body(SiteVariableRequest.builder()
                    .name(new GenerateStringUtil().generateAlphabeticString("Site", 5))
                    .type("STRING")
                    .value("bar")
                    .notes("foo bar")
                    .createdBy("#SYSTEM00000")
                    .build()
                )
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<SiteVariable> siteVariableResponse = HTTPRequest.build(requestEntity).put();

        return siteVariableResponse.getResponseEntity();
    }

    /**
     * Calls an API with GET verb
     * @param updatedSiteVariableResponse - the updated site variable
     * @return new object
     */
    public SiteVariablesItems getSiteVariable(ResponseWrapper<SiteVariable> updatedSiteVariableResponse) {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.SITE_VARIABLES, SiteVariablesItems.class)
                .queryParams(new QueryParams().use("identity[EQ]", updatedSiteVariableResponse.getResponseEntity().getIdentity()))
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<SiteVariablesItems> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity();
    }

    public void validateUpdatedObject(SiteVariable updatedSiteVariable) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(updatedType).isEqualTo(updatedSiteVariable.getType());
        softAssertions.assertThat(updatedValue).isEqualTo(updatedSiteVariable.getValue());
        softAssertions.assertThat(updatedNotes).isEqualTo(updatedSiteVariable.getNotes());
        softAssertions.assertAll();
    }

    public void validateCreatedObject(SiteVariable updatedSiteVariable) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(updatedSiteVariable.getName()).isNotBlank();
        softAssertions.assertThat(updatedSiteVariable.getIdentity()).isNotBlank();
        softAssertions.assertThat(updatedSiteVariable.getType()).isNotBlank();
        softAssertions.assertThat(updatedSiteVariable.getValue()).isNotBlank();
        softAssertions.assertThat(updatedSiteVariable.getVariableType()).isNotBlank();
        softAssertions.assertAll();
    }

    public SiteVariableRequest initUpdateRequestBody(final SiteVariable siteVariableInfo) {
        return SiteVariableRequest.builder()
            .name(updatedName)
            .value(updatedValue)
            .type("DOUBLE")
            .notes(updatedNotes)
            .updatedBy(siteVariableInfo.getCreatedBy())
            .createdBy(siteVariableInfo.getCreatedBy())
            .build();
    }
}
