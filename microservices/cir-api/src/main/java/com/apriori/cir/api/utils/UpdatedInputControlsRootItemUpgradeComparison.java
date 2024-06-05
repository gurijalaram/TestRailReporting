package com.apriori.cir.api.utils;

import com.apriori.cir.api.models.response.InputControlState;
import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "UpdatedInputControlsUpgradeComparisonResponse.json")
public class UpdatedInputControlsRootItemUpgradeComparison {
    public ArrayList<InputControlState> inputControlState;
}
