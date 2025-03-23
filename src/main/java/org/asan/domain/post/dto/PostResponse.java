package org.asan.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {

    @JsonProperty("post_id")
    private Long id;

    @JsonProperty("post_title")
    private String title;

    @JsonProperty("post_contents")
    private String content;

    @JsonProperty("post_author")
    private String author;

    @JsonProperty("timestamp")
    private String timestamp;
}
