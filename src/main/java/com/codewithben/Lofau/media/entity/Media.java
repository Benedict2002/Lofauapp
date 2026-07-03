package com.codewithben.Lofau.media.entity;

import com.codewithben.Lofau.media.enums.MediaType;
import com.codewithben.Lofau.media.enums.OwnerType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "media")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnerType ownerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String publicId;

    private String thumbnailUrl;

    private String mimeType;

    private Long fileSize;

    private Integer width;

    private Integer height;

    private Double duration;

    @Builder.Default
    private Integer sortOrder = 0;

    @Builder.Default
    private Boolean deleted = false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
