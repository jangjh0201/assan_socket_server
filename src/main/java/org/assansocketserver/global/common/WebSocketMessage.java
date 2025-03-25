package org.assansocketserver.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketMessage<T> {
    private String type;
    private Data<T> data;

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data<T> {

        @JsonProperty("total_count")
        private Integer totalCount;
        private T content; // 단일 객체 or List<T>
    }

    public static <T> WebSocketMessage<T> of(String type, T content) {
        Integer totalCount = (content instanceof java.util.List) ? ((java.util.List<?>) content).size() : null;

        return WebSocketMessage.<T>builder()
                .type(type)
                .data(new Data<>(totalCount, content))
                .build();
    }

}
