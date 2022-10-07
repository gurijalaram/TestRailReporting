package entity.request.workflow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CIFieldDefinitions {
    private CiFdKey key;
    private CiFdValue value;
}
