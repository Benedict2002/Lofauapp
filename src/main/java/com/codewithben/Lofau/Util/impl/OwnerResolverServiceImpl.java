package com.codewithben.Lofau.Util.impl;



import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.Util.OwnerResolverService;
import com.codewithben.Lofau.event.entity.Event;
import com.codewithben.Lofau.event.repository.EventRepository;
import com.codewithben.Lofau.group.entity.Group;
import com.codewithben.Lofau.group.repository.GroupRepository;
import com.codewithben.Lofau.media.enums.OwnerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerResolverServiceImpl implements OwnerResolverService {

    private final PostRepository postRepository;
    private final EventRepository eventRepository;
    private final GroupRepository groupRepository;

    @Override
    public User resolveOwner(
            UUID ownerId,
            OwnerType ownerType
    ) {

        return switch (ownerType) {

            case POST -> {

                Post post = postRepository.findById(ownerId)
                        .orElseThrow(() ->
                                new RuntimeException("Post not found"));

                yield post.getUser();
            }

            case EVENT -> {

                Event event = eventRepository.findById(ownerId)
                        .orElseThrow(() ->
                                new RuntimeException("Event not found"));

                yield event.getCreatedBy();
            }

            case GROUP -> {

                Group group = groupRepository.findById(ownerId)
                        .orElseThrow(() ->
                                new RuntimeException("Group not found"));

                yield group.getCreatedBy();
            }

            default ->
                    throw new RuntimeException(
                            "Unsupported owner type: " + ownerType
                    );
        };
    }
}
