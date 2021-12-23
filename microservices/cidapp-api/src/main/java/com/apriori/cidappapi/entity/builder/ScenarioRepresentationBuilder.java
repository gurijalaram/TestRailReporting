package com.apriori.cidappapi.entity.builder;

import com.apriori.css.entity.response.Item;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScenarioRepresentationBuilder {
    private final Item item;
    private final String lastAction;
    private final Boolean published;
    private final UserCredentials user;
}
