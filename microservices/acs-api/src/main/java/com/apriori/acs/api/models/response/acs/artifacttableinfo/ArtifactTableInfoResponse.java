package com.apriori.acs.api.models.response.acs.artifacttableinfo;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/ArtifactTableInfoResponse.json")
public class ArtifactTableInfoResponse {
    private TypeKey typeKey;
    private Boolean disableAutoProperties;
    private Boolean isDragSource;
    private Boolean isSortDisabled;
    private String beanClassName;
    private String editablePropertyNames;
    private String includedPropertyNames;
    private String leadingColumnOrder;
    private String initialSortOrder;
    private String defaultFormatterName;
    private String defaultRendererName;
    private Boolean isGroupBold;
    private Boolean isWrapped;
    private Boolean includeReadOnlyProperties;
    private String editInPlaceIndicator;
    private List<PropertyInfosItem> propertyInfos;
    private List<GroupInfosItem> groupInfos;
    private Boolean justUseIncludedPropertySet;
    private Boolean decimalPlacesOverridable;
    private Boolean cellSelectionEnabled;
    private Boolean autoResizeOff;
    private Boolean fixedFirstGroup;
    private List<String> editablePropertyNameSet;
    private List<String> includedPropertyNameSet;
    private LeadingColumnOrderIndexItem leadingColumnOrderIndex;
    @JsonProperty("transient")
    private Boolean transientValue;
    private String name;
}
