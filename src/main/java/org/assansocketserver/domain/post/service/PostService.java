package org.assansocketserver.domain.post.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assansocketserver.auth.entity.Account;
import org.assansocketserver.domain.notification.dto.NotificationDTO;
import org.assansocketserver.domain.patient.service.PatientSocketService;
import org.assansocketserver.domain.post.dto.PostRequest;
import org.assansocketserver.domain.post.dto.PostResponse;
import org.assansocketserver.domain.post.entity.Post;
import org.assansocketserver.domain.post.repository.PostRepository;
import org.assansocketserver.socket.message.NotificationMessageHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {

        private final PostRepository postRepository;
        private final NotificationMessageHandler notificationMessageHandler;

        @Transactional(readOnly = true)
        public Map<String, Object> getPosts(int pageNo) {
                Pageable pageable = PageRequest.of(pageNo - 1, 10, Sort.by("createdAt").descending());

                Page<PostResponse> page = postRepository.findAll(pageable)
                                .map(post -> PostResponse.builder()
                                                .id(post.getId())
                                                .title(post.getTitle())
                                                .author(post.getAccount().getName())
                                                .timestamp(post.getCreatedAt()
                                                                .format(DateTimeFormatter
                                                                                .ofPattern("yyyy-MM-dd HH:mm:ss")))
                                                .build());

                return Map.of(
                                "total_count", page.getTotalElements(),
                                "total_pages", page.getTotalPages(),
                                "current_page", pageNo,
                                "per_page", pageable.getPageSize(),
                                "posts", page.getContent());
        }

        @Transactional(readOnly = true)
        public PostResponse getPost(Long id) {
                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

                PostResponse response = PostResponse.builder()
                                .title(post.getTitle())
                                .content(post.getContent())
                                .author(post.getAccount().getName())
                                .timestamp(post.getCreatedAt()
                                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                .build();

                return response;
        }

        @Transactional
        public void createPost(Account account, PostRequest request) {
                Post post = postRepository.save(Post.builder()
                                .title(request.getTitle())
                                .content(request.getContent())
                                .account(account)
                                .createdAt(LocalDateTime.now())
                                .notification(request.getNotification())
                                .build());

                if (post.getNotification()) {
                        sendPostNotification(post);
                }
        }

        @Transactional
        public void updatePost(Long id, PostRequest request) {
                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

                post.update(request.getTitle(), request.getContent(), request.getNotification());

                if (post.getNotification()) {
                        sendPostNotification(post);
                }
        }

        @Transactional
        public void deletePost(Long id) {
                postRepository.deleteById(id);
        }

        private void sendPostNotification(Post post) {
                NotificationDTO notificationDTO = NotificationDTO.builder()
                                .category("notice")
                                .data(Map.of(
                                                "notice_id", post.getId(),
                                                "notice_title", post.getTitle(),
                                                "notice_author", post.getAccount().getName(),
                                                "message",
                                                String.format("%s by %s", post.getTitle(),
                                                                post.getAccount().getName()),
                                                "timestamp",
                                                LocalDateTime.now()
                                                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                                .build();
                notificationMessageHandler.sendNewNotification(notificationDTO);
        }
}
