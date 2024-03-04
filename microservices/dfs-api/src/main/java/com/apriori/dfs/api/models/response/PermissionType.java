package com.apriori.dfs.api.models.response;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

public enum PermissionType {
    CREATE,
    UPDATE,
    READ,
    DELETE,
    COST_USING;

    private PermissionType() {
    }

    public static PermissionType from(JsonNode jsonNode) {
        if (jsonNode == null) {
            return null;
        } else {
            String textValue = asText(jsonNode);
            if (StringUtils.isBlank(textValue)) {
                return null;
            } else {
                try {
                    return valueOf(asText(jsonNode));
                } catch (Exception var3) {
                    return null;
                }
            }
        }
    }

    public static String asText(JsonNode jsonNode) {
        return jsonNode != null && !jsonNode.isNull() ? StringUtils.trimToNull(jsonNode.asText()) : null;
    }
}
