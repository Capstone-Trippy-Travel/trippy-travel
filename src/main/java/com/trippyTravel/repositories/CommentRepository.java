package com.trippyTravel.repositories;

import com.trippyTravel.models.Activity;
import com.trippyTravel.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentsByActivity_Id(long activityId);
}
