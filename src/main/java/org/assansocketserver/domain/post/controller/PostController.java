package org.assansocketserver.domain.post.controller;

import java.util.Map;

import org.assansocketserver.auth.service.AccountService;
import org.assansocketserver.domain.post.dto.PostRequest;
import org.assansocketserver.domain.post.dto.PostResponse;
import org.assansocketserver.domain.post.service.PostService;
import org.assansocketserver.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

        private final AccountService accountService;
        private final PostService postService;

        // 게시글 목록 조회
        @GetMapping("")
        public ResponseEntity<RestResponse<Map<String, Object>>> getPosts(
                        @RequestParam(required = false, defaultValue = "1", value = "page") int page) {
                Map<String, Object> response = postService.getPosts(page);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));
        }

        // 특정 게시글 조회
        @GetMapping("/{id}")
        public ResponseEntity<RestResponse<PostResponse>> getPost(@PathVariable("id") Long id) {
                PostResponse response = postService.getPost(id);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));
        }

        // 게시글 등록
        @PostMapping("")
        public ResponseEntity<RestResponse<Void>> createPost(
                        @AuthenticationPrincipal UserDetails userDetails,
                        @RequestBody PostRequest request) {
                postService.createPost(accountService.getCurrentAccount(userDetails), request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(RestResponse.CREATED());
        }

        // 게시글 수정
        @PatchMapping("/{id}")
        public ResponseEntity<RestResponse<Void>> updatePost(@PathVariable("id") Long id,
                        @RequestBody PostRequest request) {
                postService.updatePost(id, request);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK());
        }

        // 게시글 삭제
        @DeleteMapping("/{id}")
        public ResponseEntity<RestResponse<Void>> deletePost(@PathVariable("id") Long id) {
                postService.deletePost(id);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK());
        }
}
