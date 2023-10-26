package com.apriori.models.response.component.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScenarioMetadata {
    private String identity;
    private List<ActiveAxes> activeAxes;
    private ActiveDimensions activeDimensions;
    private List<AxesEntry> axesEntries;
    private List<Double> boundingBox;
    private List<DrawableNode> drawableNodes;
    private List<FaceIndex> faceIndices;
    private List<Object> nodeEntries;
}
