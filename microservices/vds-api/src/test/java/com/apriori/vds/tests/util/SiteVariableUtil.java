package com.apriori.vds.tests.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.apriori.utils.GenerateStringUtil;
import com.apriori.vds.entity.request.process.group.site.variable.SiteVariableRequest;
import com.apriori.vds.entity.response.process.group.site.variable.SiteVariable;

import java.util.ArrayList;
import java.util.List;

public abstract class SiteVariableUtil extends ProcessGroupUtil {
    protected static final List<String> siteVariableIdsToDelete = new ArrayList<>();

    protected static final String updatedName = new GenerateStringUtil().generateSiteName();
    protected static final String updatedValue = "UpdatedValue";
    protected static final String updatedNotes = "UpdatedNotes";

    protected static void validateUpdatedObject(SiteVariable updatedSiteVariable) {
        assertNotEquals("The name should not be updated", updatedName, updatedSiteVariable.getName());
        assertEquals("The value should be updated", updatedValue, updatedSiteVariable.getValue());
        assertEquals("Notes should be updated", updatedNotes, updatedSiteVariable.getNotes());
    }

    protected static void validateCreatedObject(SiteVariable updatedSiteVariable) {
        assertEquals("The name should be created", updatedName, updatedSiteVariable.getName());
        assertEquals("The value should be created", updatedValue, updatedSiteVariable.getValue());
        assertEquals("Notes should be created", updatedNotes, updatedSiteVariable.getNotes());
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
