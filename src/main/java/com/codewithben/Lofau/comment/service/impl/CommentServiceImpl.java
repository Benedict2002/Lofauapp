package com.codewithben.Lofau.comment.service.impl;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.User.userService.CurrentUserService;
import com.codewithben.Lofau.Util.OwnerResolverService;
import com.codewithben.Lofau.comment.dto.request.CreateCommentRequest;
import com.codewithben.Lofau.comment.dto.response.CommentResponse;
import com.codewithben.Lofau.comment.entity.Comment;
import com.codewithben.Lofau.comment.mapper.CommentMapper;
import com.codewithben.Lofau.comment.repository.CommentRepository;
import com.codewithben.Lofau.comment.service.CommentService;
import com.codewithben.Lofau.event.repository.EventRepository;
import com.codewithben.Lofau.group.repository.GroupRepository;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.notification.dto.NotificationRequest;
import com.codewithben.Lofau.notification.enums.NotificationType;
import com.codewithben.Lofau.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final NotificationService notificationService;
    private final CurrentUserService currentUserService;
    private final EventRepository eventRepository;
    private final GroupRepository groupRepository;
    private final OwnerResolverService ownerResolverService;

    @Override
    public CommentResponse createComment(
            UUID ownerId,
            OwnerType ownerType,
            CreateCommentRequest request
    ) {

        User user = currentUserService.getCurrentUser();

        Comment comment = Comment.builder()
                .ownerId(ownerId)
                .ownerType(ownerType)
                .user(user)
                .content(request.getContent())
                .build();

        comment = commentRepository.save(comment);
        User recipient =
                ownerResolverService.resolveOwner(
                        ownerId,
                        ownerType
                );

        notificationService.notify(

                NotificationRequest.builder()

                        .recipient(recipient)
                        .actor(user)

                        .type(NotificationType.COMMENT_CREATED)

                        .referenceId(comment.getId())

                        .ownerId(ownerId)
                        .ownerType(ownerType)

                        .message(
                                user.getDisplayUsername()
                                        + " commented."
                        )

                        .build()

        );

        return commentMapper.toResponse(comment);
    }

    @Override
    public Page<CommentResponse> getComments(
            UUID ownerId,
            OwnerType ownerType,
            int page,
            int size
    ) {

        return commentRepository
                .findByOwnerIdAndOwnerTypeAndParentIsNullOrderByCreatedAtDesc(
                        ownerId,
                        ownerType,
                        PageRequest.of(page, size)
                )
                .map(commentMapper::toResponse);
    }

    @Override
    public CommentResponse replyToComment(
            UUID commentId,
            CreateCommentRequest request
    ) {

        User user = currentUserService.getCurrentUser();

        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Comment reply = Comment.builder()
                .ownerId(parent.getOwnerId())
                .ownerType(parent.getOwnerType())
                .user(user)
                .parent(parent)
                .content(request.getContent())
                .build();

        reply = commentRepository.save(reply);

        parent.setReplyCount(parent.getReplyCount() + 1);
        commentRepository.save(parent);

        notificationService.notify(

                NotificationRequest.builder()

                        .recipient(parent.getUser())
                        .actor(user)

                        .type(NotificationType.COMMENT_REPLIED)

                        .referenceId(reply.getId())

                        .ownerId(parent.getOwnerId())
                        .ownerType(parent.getOwnerType())

                        .message(
                                user.getDisplayUsername()
                                        + " replied to your comment."
                        )

                        .build()

        );

        return commentMapper.toResponse(reply);
    }

    @Override
    public List<CommentResponse> getReplies(
            UUID commentId
    ) {

        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        return commentRepository
                .findByParentOrderByCreatedAtAsc(parent)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}