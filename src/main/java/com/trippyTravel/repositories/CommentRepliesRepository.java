package com.trippyTravel.repositories;

import com.trippyTravel.models.Comment;
import com.trippyTravel.models.CommentReplies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepliesRepository extends JpaRepository<CommentReplies, Long> {
    boolean existsCommentRepliesByParentComment(Comment comment);
    List<CommentReplies> findCommentRepliesByParentComment(Comment comment);
}
