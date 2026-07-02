package com.codewithben.Lofau.Post.entity;

import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostStatus;
import com.codewithben.Lofau.Post.enums.PostType;
import com.codewithben.Lofau.Post.enums.Visibility;
import com.codewithben.Lofau.User.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private BigDecimal rewardAmount;

    @Builder.Default
    private Boolean approved = true;

    @Builder.Default
    private Boolean edited = false;

    @Builder.Default
    private Boolean anonymous = false;

    @Builder.Default
    private Boolean commentsEnabled = true;

    @Builder.Default
    private Integer views = 0;

    @Builder.Default
    private Integer likes = 0;

    @Builder.Default
    private Integer shares = 0;

    @Builder.Default
    private Integer commentCount = 0;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<PostMedia> media = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if(status == null)
            status = PostStatus.ACTIVE;

        if(visibility == null)
            visibility = Visibility.PUBLIC;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
