package com.apriori.cir.api.models.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<String> getAllOptions() {
        return options.stream().flatMap(e -> Stream.of(e.getLabel())).collect(Collectors.toList());
    }
}
