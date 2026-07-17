package com.codewithben.Lofau.Post.service.impl;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.entity.PostLike;
import com.codewithben.Lofau.Post.entity.SavedPost;
import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostType;
import com.codewithben.Lofau.Post.mapper.PostMapper;
import com.codewithben.Lofau.Post.repository.PostLikeRepository;
import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.Post.repository.SavedPostRepository;
import com.codewithben.Lofau.Post.service.PostService;
import com.codewithben.Lofau.search.specification.PostSpecification;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.group.entity.Group;
import com.codewithben.Lofau.group.entity.GroupMember;
import com.codewithben.Lofau.group.enums.MemberStatus;
import com.codewithben.Lofau.group.repository.GroupMemberRepository;
import com.codewithben.Lofau.group.repository.GroupRepository;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import com.codewithben.Lofau.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final MediaService mediaService;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PostLikeRepository postLikeRepository;
    private final NotificationService notificationService;
    private final SavedPostRepository savedPostRepository;

    @Override
    public PostResponse createPost(
            CreatePostRequest request,
            List<MultipartFile> files
    ) throws IOException {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("No authenticated user found");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postMapper.toEntity(request);

        post.setUser(user);

        /*
         * GROUP POST
         */
        if (request.getGroupId() != null) {

            Group group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() ->
                            new RuntimeException("Group not found"));

            GroupMember member = groupMemberRepository
                    .findByGroupAndUser(group, user)
                    .orElseThrow(() ->
                            new RuntimeException("You are not a member of this group"));

            if (member.getStatus() != MemberStatus.APPROVED) {
                throw new RuntimeException(
                        "Your membership has not been approved."
                );
            }

            post.setGroup(group);
        }

        Post savedPost = postRepository.save(post);

        if (files != null && !files.isEmpty()) {

            mediaService.saveMedia(

                    savedPost.getId(),

                    files,

                    OwnerType.POST

            );

        }

        return postMapper.toResponse(savedPost);

    }

    @Override
    public PostResponse likePost(UUID postId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (postLikeRepository.existsByPostAndUser(post, user)) {
            return postMapper.toResponse(post);
        }

        PostLike like = PostLike.builder()
                .post(post)
                .user(user)
                .build();

        postLikeRepository.save(like);

        refreshLikeCount(post);

        notificationService.notifyPostLiked(user, post);

        return postMapper.toResponse(post);
    }

    @Override
    public PostResponse unlikePost(UUID postId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostLike like = postLikeRepository.findByPostAndUser(
                post,
                user
        ).orElse(null);

        if (like == null) {
            return postMapper.toResponse(post);
        }


        postLikeRepository.delete(like);

        refreshLikeCount(post);

        return postMapper.toResponse(post);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getFeed(Pageable pageable) {

        return postRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .map(postMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        return postMapper.toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getPostsByUser(
            Long userId,
            Pageable pageable
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return postRepository
                .findByUserOrderByCreatedAtDesc(user, pageable)
                .map(postMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getPostsByGroup(
            UUID groupId,
            Pageable pageable
    ) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        return postRepository
                .findByGroupOrderByCreatedAtDesc(group, pageable)
                .map(postMapper::toResponse);
    }

    @Override
    public PostResponse savePost(UUID postId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        if (savedPostRepository.existsByUserAndPost(user, post)) {
            return postMapper.toResponse(post);
        }

        SavedPost savedPost = SavedPost.builder()
                .user(user)
                .post(post)
                .build();

        savedPostRepository.save(savedPost);

        return postMapper.toResponse(post);
    }

    @Override
    public PostResponse unsavePost(UUID postId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        SavedPost savedPost = savedPostRepository
                .findByUserAndPost(user, post)
                .orElse(null);

        if (savedPost == null) {
            return postMapper.toResponse(post);
        }

        savedPostRepository.delete(savedPost);

        return postMapper.toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getSavedPosts(Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return savedPostRepository
                .findByUserOrderByCreatedAtDesc(user, pageable)
                .map(savedPost ->
                        postMapper.toResponse(savedPost.getPost()));
    }


    private void refreshLikeCount(Post post) {

        post.setLikes((int) postLikeRepository.countByPost(post));

        postRepository.save(post);
    }




//    @Override
//    public PostResponse createPost(
//            CreatePostRequest request,
//            List<MultipartFile> files
//    ) throws IOException {
//
//        Authentication authentication =
//                SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("No authenticated user found");
//        }
//
//        String email = authentication.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Post post = postMapper.toEntity(request);
//
//        post.setUser(user);
//
//        Post savedPost = postRepository.save(post);
//
//        // Upload images after the post has been saved
//        mediaService.saveMedia(
//                savedPost.getId(),
//                files,
//                OwnerType.POST
//        );
//
//        // Return the complete post with media
//        return postMapper.toResponse(savedPost);
//    }

}