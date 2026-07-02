package com.codewithben.Lofau.Post.repository;

import com.codewithben.Lofau.Post.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostMediaRepository
        extends JpaRepository<PostMedia, UUID> {
}
