package com.apriori.acs.entity.response.acs.designGuidance;

import lombok.Data;

import java.util.ArrayList;

@Data
public class InfosByTopics {
    private ArrayList<GenericGuidanceTopics> genericGuidanceTopics;
}
