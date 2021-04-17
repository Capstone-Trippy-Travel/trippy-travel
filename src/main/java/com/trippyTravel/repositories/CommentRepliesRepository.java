package com.trippyTravel.repositories;

import com.trippyTravel.models.CommentReplies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepliesRepository extends JpaRepository<CommentReplies, Long> {
}
