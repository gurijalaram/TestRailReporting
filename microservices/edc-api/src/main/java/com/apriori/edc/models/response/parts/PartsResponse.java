package com.apriori.edc.models.response.parts;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Schema(location = "Parts.json")
@Data

public class PartsResponse extends ArrayList {
    private List<Parts> parts;
}
