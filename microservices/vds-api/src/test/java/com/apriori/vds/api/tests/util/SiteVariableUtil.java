package com.apriori.vds.api.tests.util;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariable;

import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;

public class SiteVariableUtil {
    private RequestEntityUtil requestEntityUtil;
    protected static final String updatedName = new GenerateStringUtil().generateAlphabeticString("Site", 5);
    protected static final String updatedType = "STRING";
    protected static final String updatedValue = "UpdatedValue";
    protected static final String updatedNotes = "UpdatedNotes";

    public SiteVariableUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    public SiteVariable putSiteVariable(String processGroupId, SiteVariable siteVariableBeforeUpdate) {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PROCESS_GROUP_SITE_VARIABLE_BY_PG_ID, SiteVariable.class)
                .inlineVariables(new ProcessGroupUtil(requestEntityUtil).getProcessGroupIdentity())
                .body(initUpdateRequestBody(siteVariableBeforeUpdate))
                .expectedResponseCode(HttpStatus.SC_CREATED);

        final ResponseWrapper<SiteVariable> updatedSiteVariableResponse = HTTPRequest.build(requestEntity).put();

        return updatedSiteVariableResponse.getResponseEntity();
    }

    public void deleteProcessGroupSiteVariableByName(final String name) {
        RequestEntity requestEntity =
            requestEntityUtil.init(VDSAPIEnum.PROCESS_GROUP_SITE_VARIABLE_BY_PG_SITE_IDs, null)
                .inlineVariables(new ProcessGroupUtil(requestEntityUtil).getProcessGroupIdentity(), name)
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    public SiteVariable postProcessGroupSiteVariables() {
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
