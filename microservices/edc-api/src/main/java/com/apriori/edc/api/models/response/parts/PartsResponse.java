package com.apriori.edc.api.models.response.parts;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Schema(location = "Parts.json")
@Data

public class PartsResponse extends ArrayList {
    private List<Parts> parts;
}
