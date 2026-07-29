package com.codewithben.Lofau.Post.service.impl;

import com.codewithben.Lofau.Exception.group.GroupException;
import com.codewithben.Lofau.Exception.post.PostException;
import com.codewithben.Lofau.Exception.user.UserException;
import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.request.UpdatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.entity.PostLike;
import com.codewithben.Lofau.Post.entity.PostView;
import com.codewithben.Lofau.Post.entity.SavedPost;
import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostType;
import com.codewithben.Lofau.Post.mapper.PostMapper;
import com.codewithben.Lofau.Post.repository.PostLikeRepository;
import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.Post.repository.PostViewRepository;
import com.codewithben.Lofau.Post.repository.SavedPostRepository;
import com.codewithben.Lofau.Post.service.PostService;
import com.codewithben.Lofau.User.userService.CurrentUserService;
import com.codewithben.Lofau.notification.dto.NotificationRequest;
import com.codewithben.Lofau.notification.enums.NotificationType;
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
import java.time.LocalDateTime;
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
    private final PostViewRepository postViewRepository;
    private final CurrentUserService currentUserService;

    @Override
    public PostResponse createPost(
            CreatePostRequest request,
            List<MultipartFile> files
    ) throws IOException {

        User user = currentUserService.getCurrentUser();

        Post post = postMapper.toEntity(request);

        post.setUser(user);

        /*
         * GROUP POST
         */
        if (request.getGroupId() != null) {

            Group group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() ->
                            new GroupException("Group not found"));

            GroupMember member = groupMemberRepository
                    .findByGroupAndUser(group, user)
                    .orElseThrow(() ->
                            new GroupException("You are not a member of this group"));

            if (member.getStatus() != MemberStatus.APPROVED) {
                throw new GroupException(
                        "Your membership has not been approved."
                );
            }

            post.setGroup(group);
        }

        Post savedPost = postRepository.save(post);

        if (files != null && !files.isEmpty()) {

            mediaService.uploadGallery(
                    savedPost.getId(),
                    files,
                    OwnerType.POST
            );

        }

        return postMapper.toResponse(savedPost);

    }

    @Override
    public PostResponse likePost(UUID postId) {

        User user = currentUserService.getCurrentUser();
        Post post = getPost(postId);
        if (postLikeRepository.existsByPostAndUser(post, user)) {
            return postMapper.toResponse(post);
        }

        PostLike like = PostLike.builder()
                .post(post)
                .user(user)
                .build();

        postLikeRepository.save(like);

        refreshLikeCount(post);

        notificationService.notify(

                NotificationRequest.builder()

                        .recipient(post.getUser())
                        .actor(user)

                        .type(NotificationType.POST_LIKED)

                        .referenceId(post.getId())

                        .ownerId(post.getId())
                        .ownerType(OwnerType.POST)

                        .message(
                                user.getDisplayUsername()
                                        + " liked your post."
                        )

                        .build()

        );

        return postMapper.toResponse(post);
    }

    @Override
    public PostResponse unlikePost(UUID postId) {

        User user = currentUserService.getCurrentUser();
        Post post = getPost(postId);

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
                .findByDeletedFalseOrderByCreatedAtDesc(pageable)
                .map(postMapper::toResponse);
    }

    @Override
    @Transactional
    public PostResponse getPostById(UUID postId) {

        User user = currentUserService.getCurrentUser();
        Post post = getPost(postId);

        boolean alreadyViewed =
                postViewRepository.existsByPostAndUser(post, user);

        if (!alreadyViewed) {

            PostView postView = PostView.builder()
                    .post(post)
                    .user(user)
                    .build();

            postViewRepository.save(postView);

            post.setViews(post.getViews() + 1);

            postRepository.save(post);
        }

        return postMapper.toResponse(post);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getPostsByUser(
            UUID userId,
            Pageable pageable
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserException("User not found"));

        return postRepository
                .findByUserAndDeletedFalseOrderByCreatedAtDesc(user, pageable)
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
                        new GroupException("Group not found"));

        return postRepository
                .findGroupFeed(group, pageable)
                .map(postMapper::toResponse);
    }

    @Override
    public PostResponse savePost(UUID postId) {

        User user = currentUserService.getCurrentUser();
        Post post = getPost(postId);

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

        User user = currentUserService.getCurrentUser();
        Post post = getPost(postId);

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

        User user = currentUserService.getCurrentUser();
        return savedPostRepository
                .findByUserOrderByCreatedAtDesc(user, pageable)
                .map(savedPost ->
                        postMapper.toResponse(savedPost.getPost()));
    }

    @Override
    public PostResponse sharePost(UUID postId) {


        Post post = getPost(postId);
        post.setShares(post.getShares() + 1);

        postRepository.save(post);

        return postMapper.toResponse(post);
    }

    @Override
    public PostResponse updatePost(
            UUID postId,
            UpdatePostRequest request,
            List<MultipartFile> files
    ) throws IOException {

        User user = currentUserService.getCurrentUser();
        Post post = getPost(postId);
        // Only the owner can edit the post
        if (!post.getUser().getId().equals(user.getId())) {
            throw new PostException(
                    "You are not allowed to edit this post."
            );
        }

        postMapper.updateEntity(post, request);

        post.setUpdatedAt(LocalDateTime.now());

        post = postRepository.save(post);

        if (files != null && !files.isEmpty()) {

            mediaService.uploadGallery(
                    post.getId(),
                    files,
                    OwnerType.POST
            );

        }

        return postMapper.toResponse(post);
    }


    @Override
    public void deletePost(UUID postId) {

        User user = currentUserService.getCurrentUser();
        Post post = getPost(postId);
        // Only the owner can delete
        if (!post.getUser().getId().equals(user.getId())) {
            throw new PostException(
                    "You are not allowed to delete this post."
            );
        }

        post.setDeleted(true);
        post.setDeletedAt(LocalDateTime.now());

        postRepository.save(post);

    }


    private void refreshLikeCount(Post post) {

        post.setLikes((int) postLikeRepository.countByPost(post));

        postRepository.save(post);
    }

    private Post getPost(UUID postId) {

        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new PostException("Post not found"));
    }





}