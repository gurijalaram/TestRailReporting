package com.apriori.trr.api.testrail.internal;

import static com.google.common.base.Preconditions.checkArgument;

import com.apriori.trr.api.testrail.model.Case;
import com.apriori.trr.api.testrail.model.CaseField;
import com.apriori.trr.api.testrail.model.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.base.Function;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jackson module for {@link com.apriori.trr.api.testrail.model.Case}.
 * <p/>
 * INTERNAL ONLY
 */
public class CaseModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext setupContext) {
        setupContext.addBeanDeserializerModifier(new CaseDeserializerModifier());
        super.setupModule(setupContext);
    }

    private static class CaseDeserializer extends StdDeserializer<Case> implements ResolvableDeserializer {
        private final JsonDeserializer<?> defaultDeserializer;

        CaseDeserializer(JsonDeserializer<?> defaultDeserializer) {
            super(Case.class);
            this.defaultDeserializer = defaultDeserializer;
        }

        @Override
        public Case deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Case testCase = (Case) defaultDeserializer.deserialize(jsonParser, deserializationContext);

            ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
            List<CaseField> caseFieldList = (List<CaseField>) deserializationContext.findInjectableValue(Case.class.toString(), null, null);
            Map<String, CaseField> caseFields = Maps.uniqueIndex(caseFieldList, new Function<CaseField, String>() {
                @Override
                public String apply(final CaseField caseField) {
                    return caseField.getName();
                }
            });
            Map<String, Object> customFields = new HashMap<>(testCase.getCustomFields().size());
            for (Map.Entry<String, Object> customField : testCase.getCustomFields().entrySet()) {
                checkArgument(caseFields.containsKey(customField.getKey()), "Case field list configuration is possibly outdated since it does not contain custom field: " + customField.getKey());
                customFields.put(customField.getKey(), mapper.convertValue(customField.getValue(), Field.Type.getType(caseFields.get(customField.getKey()).getTypeId()).getTypeReference()));
            }
            testCase.setCustomFields(customFields);
            return testCase;
        }

        @Override
        public void resolve(DeserializationContext deserializationContext) throws JsonMappingException {
            ((ResolvableDeserializer) defaultDeserializer).resolve(deserializationContext);
        }
    }

    private static class CaseDeserializerModifier extends BeanDeserializerModifier {

        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig deserializationConfig, BeanDescription beanDescription, JsonDeserializer<?> jsonDeserializer) {
            if (Case.class.isAssignableFrom(beanDescription.getBeanClass())) {
                return new CaseDeserializer(jsonDeserializer);
            }
            return jsonDeserializer;
        }

    }
}
