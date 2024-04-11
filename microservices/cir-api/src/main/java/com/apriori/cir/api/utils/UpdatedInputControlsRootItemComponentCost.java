package com.apriori.cir.api.utils;

import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Schema(location = "UpdatedInputControlsComponentCostResponse.json")
@Data
public class UpdatedInputControlsRootItemComponentCost {
    public ArrayList<InputControlState> inputControlState;
}
