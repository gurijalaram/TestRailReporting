package com.apriori.cir.models.response;

import lombok.Data;

import java.util.List;

@Data
public class InputControlState {
    private String uri;
    private String value;
    private String error;
    private String id;
    private String totalCount;
    private List<InputControlOption> options;

    public InputControlOption getOption(final String name) {
        return options.stream().filter(option -> option.getLabel().equals(name))
            .findFirst().orElse(null);
    }
}
