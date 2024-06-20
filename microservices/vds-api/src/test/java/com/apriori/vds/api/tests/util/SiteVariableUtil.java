package com.apriori.vds.api.tests.util;

import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.vds.api.models.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.api.models.response.process.group.site.variable.SiteVariable;

import org.assertj.core.api.SoftAssertions;

public abstract class SiteVariableUtil extends ProcessGroupUtil {

    protected static final String updatedName = new GenerateStringUtil().generateAlphabeticString("Site", 5);
    protected static final String updatedType = "STRING";
    protected static final String updatedValue = "UpdatedValue";
    protected static final String updatedNotes = "UpdatedNotes";

    protected static void validateUpdatedObject(SiteVariable updatedSiteVariable) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(updatedType).isEqualTo(updatedSiteVariable.getType());
        softAssertions.assertThat(updatedValue).isEqualTo(updatedSiteVariable.getValue());
        softAssertions.assertThat(updatedNotes).isEqualTo(updatedSiteVariable.getNotes());
        softAssertions.assertAll();
    }

    protected static void validateCreatedObject(SiteVariable updatedSiteVariable) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(updatedSiteVariable.getName()).isNotBlank();
        softAssertions.assertThat(updatedSiteVariable.getIdentity()).isNotBlank();
        softAssertions.assertThat(updatedSiteVariable.getType()).isNotBlank();
        softAssertions.assertThat(updatedSiteVariable.getValue()).isNotBlank();
        softAssertions.assertThat(updatedSiteVariable.getVariableType()).isNotBlank();
        softAssertions.assertAll();
    }

    protected SiteVariableRequest initUpdateRequestBody(final SiteVariable siteVariableInfo) {
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
