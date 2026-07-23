package com.codewithben.Lofau.notification.factoryN;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.comment.entity.Comment;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.notification.entity.Notification;
import com.codewithben.Lofau.notification.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {

    public Notification createPostLiked(User actor, Post post) {

        return Notification.builder()
                .recipient(post.getUser())
                .actor(actor)
                .type(NotificationType.POST_LIKED)
                .referenceId(post.getId())
                .previewOwnerId(post.getId())
                .previewOwnerType(OwnerType.POST)
                .actionUrl("/posts/" + post.getId())
                .message(actor.getDisplayUsername() + " liked your post.")
                .build();
    }

    public Notification createPostCommented(User actor, Post post) {

        return Notification.builder()
                .recipient(post.getUser())
                .actor(actor)
                .type(NotificationType.POST_COMMENTED)
                .referenceId(post.getId())
                .previewOwnerId(post.getId())
                .previewOwnerType(OwnerType.POST)
                .actionUrl("/posts/" + post.getId())
                .message(actor.getDisplayUsername() + " commented on your post.")
                .build();
    }

    public Notification createCommentReply(User actor, Comment comment) {

        return Notification.builder()
                .recipient(comment.getUser())
                .actor(actor)
                .type(NotificationType.COMMENT_REPLIED)
                .referenceId(comment.getId())
                .previewOwnerId(comment.getPost().getId())
                .previewOwnerType(OwnerType.POST)
                .actionUrl("/posts/" + comment.getPost().getId())
                .message(actor.getDisplayUsername() + " replied to your comment.")
                .build();
    }

}