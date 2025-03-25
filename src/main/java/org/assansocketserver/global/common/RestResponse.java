package org.assansocketserver.global.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 최상위 응답 포맷
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestResponse<T> {

    private int status;
    private String message;
    private T data;

    public static RestResponse<Void> OK() {
        return RestResponse.<Void>builder()
                .status(200)
                .message("OK")
                .data(null)
                .build();
    }

    public static <T> RestResponse<T> OK(T data) {
        return RestResponse.<T>builder()
                .status(200)
                .message("OK")
                .data(data)
                .build();
    }

    public static RestResponse<Void> CREATED() {
        return RestResponse.<Void>builder()
                .status(201)
                .message("CREATED")
                .data(null)
                .build();
    }

    public static <T> RestResponse<T> CREATED(T data) {
        return RestResponse.<T>builder()
                .status(201)
                .message("CREATED")
                .data(data)
                .build();
    }

    public static RestResponse<Void> CONFLICT() {
        return RestResponse.<Void>builder()
                .status(409)
                .message("CONFLICT")
                .data(null)
                .build();
    }

    public static RestResponse<Void> UNAUTHORIZED() {
        return RestResponse.<Void>builder()
                .status(401)
                .message("UNAUTHORIZED")
                .data(null)
                .build();
    }

    public static RestResponse<Void> FORBIDDEN() {
        return RestResponse.<Void>builder()
                .status(403)
                .message("FORBIDDEN")
                .data(null)
                .build();
    }

}
