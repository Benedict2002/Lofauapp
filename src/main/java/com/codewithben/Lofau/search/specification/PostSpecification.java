package com.codewithben.Lofau.search.specification;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostType;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {

    public static Specification<Post> search(

            String keyword,

            PostType postType,

            Category category,

            String location

    ) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            /*
             * Keyword
             */
            if (keyword != null && !keyword.isBlank()) {

                String like = "%" + keyword.toLowerCase() + "%";

                predicate = cb.and(
                        predicate,
                        cb.or(
                                cb.like(
                                        cb.lower(root.get("title")),
                                        like
                                ),
                                cb.like(
                                        cb.lower(root.get("description")),
                                        like
                                ),
                                cb.like(
                                        cb.lower(root.get("locationName")),
                                        like
                                )
                        )
                );
            }

            /*
             * Post Type
             */
            if (postType != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("postType"),
                                postType
                        )
                );
            }

            /*
             * Category
             */
            if (category != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("category"),
                                category
                        )
                );
            }

            /*
             * Location
             */
            if (location != null && !location.isBlank()) {

                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(root.get("locationName")),
                                "%" + location.toLowerCase() + "%"
                        )
                );
            }

            query.orderBy(
                    cb.desc(root.get("createdAt"))
            );

            return predicate;
        };

    }

}