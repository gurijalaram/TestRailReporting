package entity.request.workflow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QdLogicalFilter {
    private String filterType;
    private ArrayList<QdLfRuleFilters> ruleFilters;
}
