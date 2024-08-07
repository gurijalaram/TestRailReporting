package com.apriori.trr.api.testrail.internal;

import static com.google.common.base.Preconditions.checkArgument;

import com.apriori.trr.api.testrail.model.Field;
import com.apriori.trr.api.testrail.model.Result;
import com.apriori.trr.api.testrail.model.ResultField;

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
 * Jackson module for {@link com.apriori.trr.api.testrail.model.Result}.
 * <p/>
 * INTERNAL ONLY
 */
public class ResultModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext setupContext) {
        setupContext.addBeanDeserializerModifier(new ResultDeserializerModifier());
        super.setupModule(setupContext);
    }

    private static class ResultDeserializer extends StdDeserializer<Result> implements ResolvableDeserializer {
        private final JsonDeserializer<?> defaultDeserializer;

        ResultDeserializer(JsonDeserializer<?> defaultDeserializer) {
            super(Result.class);
            this.defaultDeserializer = defaultDeserializer;
        }

        @Override
        public Result deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Result result = (Result) defaultDeserializer.deserialize(jsonParser, deserializationContext);

            ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
            List<ResultField> resultFieldList = (List<ResultField>) deserializationContext.findInjectableValue(Result.class.toString(), null, null);
            Map<String, ResultField> resultFields = Maps.uniqueIndex(resultFieldList, new Function<ResultField, String>() {
                @Override
                public String apply(final ResultField resultField) {
                    return resultField.getName();
                }
            });
            Map<String, Object> customFields = new HashMap<>(result.getCustomFields().size());
            for (Map.Entry<String, Object> customField : result.getCustomFields().entrySet()) {
                checkArgument(resultFields.containsKey(customField.getKey()), "Result field list configuration is possibly outdated since it does not contain custom field: " + customField.getKey());
                customFields.put(customField.getKey(), mapper.convertValue(customField.getValue(), Field.Type.getType(resultFields.get(customField.getKey()).getTypeId()).getTypeReference()));
            }
            result.setCustomFields(customFields);
            return result;
        }

        @Override
        public void resolve(DeserializationContext deserializationContext) throws JsonMappingException {
            ((ResolvableDeserializer) defaultDeserializer).resolve(deserializationContext);
        }
    }

    private static class ResultDeserializerModifier extends BeanDeserializerModifier {

        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig deserializationConfig, BeanDescription beanDescription, JsonDeserializer<?> jsonDeserializer) {
            if (Result.class.isAssignableFrom(beanDescription.getBeanClass())) {
                return new ResultDeserializer(jsonDeserializer);
            }
            return jsonDeserializer;
        }

    }
}