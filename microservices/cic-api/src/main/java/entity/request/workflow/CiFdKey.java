package entity.request.workflow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CiFdKey {
    private String name;
    private String description;
    private String baseType;
    private int ordinal;
    private Aspects aspects;
}
