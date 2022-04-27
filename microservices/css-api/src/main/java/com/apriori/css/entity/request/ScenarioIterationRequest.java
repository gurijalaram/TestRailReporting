package com.apriori.css.entity.request;

import com.apriori.css.entity.enums.Direction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScenarioIterationRequest {
    private Query query;
    private Paging paging;
    private List<Sorting> sorting;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Query {
        private LogicalOperator filter;
        private Map<String, Object> parameters;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class LogicalOperator {
            private List<Operator> and;
            private List<Operator> or;
            private Operator not;

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class Operator {
                private Params equals;
                private Params starts;
                private Params in;
                private Params between;
                private Params isNull;
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class Params {
                private String property;
                private Object value;
                private List<Object> values;
                private Object min;
                private Object max;
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Paging {
        private Integer pageNumber;
        private Integer pageSize;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Sorting {
        private String property;
        private Direction direction;
    }
}
