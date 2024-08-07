package com.apriori.trr.api.testrail.model;

import static com.apriori.trr.api.testrail.model.Field.Type;

import com.apriori.trr.api.testrail.TestRail;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.google.common.base.MoreObjects;
import lombok.Data;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * TestRail case.
 */
@Data
public class Case {

    private static final String CUSTOM_FIELD_KEY_PREFIX = "custom__";

    private int id;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private String title;

    private int sectionId;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private Integer typeId;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private Integer priorityId;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private Integer milestoneId;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private String refs;

    private int createdBy;

    private Date createdOn;

    private int updatedBy;

    private Date updatedOn;

    @JsonView({TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    private String estimate;

    private String estimateForecast;

    private String custom_automation_type;

    private int suiteId;

    @JsonView({TestRail.Cases.Get.class, TestRail.Cases.Add.class, TestRail.Cases.Update.class})
    @JsonIgnore
    private Map<String, Object> customFields;

    @JsonAnyGetter
    @JsonSerialize(keyUsing = CustomFieldSerializer.class)
    public Map<String, Object> getCustomFields() {
        return MoreObjects.firstNonNull(customFields, Collections.<String, Object>emptyMap());
    }

    /**
     * Add a custom field.
     *
     * @param key   the name of the custom field with or without "custom_" prefix
     * @param value the value of the custom field
     * @return case instance for chaining
     */
    public Case addCustomField(String key, Object value) {
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

    /**
     * Get custom field.
     * <p>Use Java Type Inference, to get the value with correct type. Refer to {@link Type} for a map of TestRail field types to Java types.</p>
     *
     * @param key the system name of custom field
     * @param <T> the type of returned value
     * @return the value of the custom field
     */
    public <T> T getCustomField(String key) {
        return (T) getCustomFields().get(key);
    }

    /**
     * Serializer for custom fields.
     */
    private static class CustomFieldSerializer extends StdKeySerializer {

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            super.serialize(CUSTOM_FIELD_KEY_PREFIX.replace("__", "_") + o, jsonGenerator, serializerProvider);
        }
    }

}
