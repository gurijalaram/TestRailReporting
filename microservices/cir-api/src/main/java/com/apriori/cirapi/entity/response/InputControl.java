package com.apriori.cirapi.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Schema(location = "InputControlResponse.json")
@Data
public class InputControl {
    @JsonProperty("inputControlState")
    private List<InputControlState> inputControls;

    public InputControlState getMassMetric() {
        return getPropertyValueIfExist("massMetric");
    }

    public InputControlState getCostMetric() {
        return getPropertyValueIfExist("costMetric");
    }

    public InputControlState getExportSetName() {
        return getPropertyValueIfExist("exportSetName");
    }

    public InputControlState getRollup() {
        return getPropertyValueIfExist("rollup");
    }

    public InputControlState getExportEventId() {
        return getPropertyValueIfExist("exportEventId");
    }

    public InputControlState getMetricStatistic() {
        return getPropertyValueIfExist("metricStatistic");
    }

    private InputControlState getPropertyValueIfExist(final String propertyName) {
        return getInputControlStateByName(propertyName);
    }

    public InputControlState getInputControlStateByName(final String name) {
        return inputControls.stream().filter(inputControlState -> inputControlState.getId().equals(name))
            .findFirst().orElse(null);
    }
}
