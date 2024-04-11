package com.apriori.cir.api.utils;

import java.util.ArrayList;

import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.shared.util.annotations.Schema;
import lombok.Data;

@Schema(location = "InputControlsUpdatedRootItemComponentCost.json")
@Data
public class UpdatedInputControlsRootItemComponentCost {
    public ArrayList<InputControlState> inputControlState;
}
