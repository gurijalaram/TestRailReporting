package com.apriori.util;

import com.apriori.GenerateStringUtil;
import com.apriori.vds.models.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.models.response.process.group.site.variable.SiteVariable;

import org.assertj.core.api.SoftAssertions;

public abstract class SiteVariableUtil extends ProcessGroupUtil {

    protected static final String updatedName = new GenerateStringUtil().generateSiteName();
    protected static final String updatedValue = "UpdatedValue";
    protected static final String updatedNotes = "UpdatedNotes";

    protected static void validateUpdatedObject(SiteVariable updatedSiteVariable) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(updatedName).isNotEqualTo(updatedSiteVariable.getName());
        softAssertions.assertThat(updatedValue).isEqualTo(updatedSiteVariable.getValue());
        softAssertions.assertThat(updatedNotes).isEqualTo(updatedSiteVariable.getNotes());
        softAssertions.assertAll();
    }

    protected static void validateCreatedObject(SiteVariable updatedSiteVariable) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(updatedName).isEqualTo(updatedSiteVariable.getName());
        softAssertions.assertThat(updatedValue).isEqualTo(updatedSiteVariable.getValue());
        softAssertions.assertThat(updatedNotes).isEqualTo(updatedSiteVariable.getNotes());
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
