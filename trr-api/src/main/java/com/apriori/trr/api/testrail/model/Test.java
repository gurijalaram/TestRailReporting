package com.apriori.trr.api.testrail.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.google.common.base.MoreObjects;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * TestRail test.
 */
@Data
public class Test {

    private static final String CUSTOM_FIELD_KEY_PREFIX = "custom_";

    private int id;

    private int caseId;

    private Integer assignedtoId;

    private String title;

    private int statusId;

    private int typeId;

    private int priorityId;

    private Integer milestoneId;

    private Integer runId;

    private String refs;

    private String estimate;

    private String estimateForecast;

    private Map<String, Object> customFields;

    public Map<String, Object> getCustomFields() {
        return MoreObjects.firstNonNull(customFields, Collections.<String, Object>emptyMap());
    }

    /**
     * Add a custom field.
     *
     * @param key   the name of the custom field with or without "custom_" prefix
     * @param value the value of the custom field
     * @return test instance for chaining
     */
    public Test addCustomField(String key, Object value) {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        customFields.put(key.replaceFirst(CUSTOM_FIELD_KEY_PREFIX, ""), value);
        return this;
    }

    /**
     * Support for forward compatibility and extracting custom fields.
     *
     * @param key the name of the unknown field
     * @param value the value of the unkown field
     */
    @JsonAnySetter
    private void addUnknownField(String key, Object value) {
        if (key.startsWith(CUSTOM_FIELD_KEY_PREFIX)) {
            addCustomField(key, value);
        }
    }

}
