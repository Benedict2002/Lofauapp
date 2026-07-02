package com.codewithben.Lofau.Post.entity;

import com.codewithben.Lofau.Post.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="post_media")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    private Integer displayOrder;

    private LocalDateTime uploadedAt;

    @PrePersist
    public void prePersist(){
        uploadedAt = LocalDateTime.now();
    }
}
