package com.apriori.cir.api.utils;

import java.util.ArrayList;

import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.shared.util.annotations.Schema;
import lombok.Data;

@Schema(location = "UpdatedInputControlsAssemblyCostA4Response.json")
@Data
public class UpdatedInputControlsRootItemAssemblyCostA4 {
    public ArrayList<InputControlState> inputControlState;
}
