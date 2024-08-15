package com.apriori.trr.api.testrail.model;

import static com.apriori.trr.api.testrail.model.Field.Type;

import com.apriori.trr.api.testrail.TestRail;
import com.apriori.trr.api.testrail.internal.CsvToListDeserializer;
import com.apriori.trr.api.testrail.internal.ListToCsvSerializer;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TestRail result.
 */
@Data
@ToString(exclude = "caseId")
public class Result {

    private static final String CUSTOM_FIELD_KEY_PREFIX = "custom__";

    private int id;

    private int testId;

    @JsonView({TestRail.Results.AddListForCases.class})
    private Integer caseId;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    private Integer statusId;

    private Date createdOn;

    private int createdBy;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    private Integer assignedToId;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    private String comment;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    private String version;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    private String elapsed;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    @JsonSerialize(using = ListToCsvSerializer.class)
    @JsonDeserialize(using = CsvToListDeserializer.class)
    private java.util.List<String> defects;

    @JsonView({TestRail.Results.Add.class, TestRail.Results.AddForCase.class, TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
    @JsonIgnore
    private Map<String, Object> customFields;

    /**
     * Add a defect.
     *
     * @param defect defect to be added
     * @return this instance for chaining
     */
    public Result addDefect(@NonNull String defect) {
        Preconditions.checkArgument(!defect.isEmpty(), "defect cannot be empty");
        java.util.List<String> defects = getDefects();
        if (defects == null) {
            defects = new ArrayList<>();
        }
        defects.add(defect);
        setDefects(defects);
        return this;
    }

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
     * @return result instance for chaining
     */
    public Result addCustomField(String key, Object value) {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        customFields.put(key.replaceFirst(CUSTOM_FIELD_KEY_PREFIX, ""), value);
        return this;
    }

    /**
     * Support for forward compatibility and extracting custom fields.
     *
     * @param key   the name of the unknown field
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
            super.serialize(CUSTOM_FIELD_KEY_PREFIX + o, jsonGenerator, serializerProvider);
        }
    }

    /**
     * Wrapper for list of {@code Result}s for internal use.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class List {

        @JsonView({TestRail.Results.AddList.class, TestRail.Results.AddListForCases.class})
        private java.util.List<Result> results;

    }
}
