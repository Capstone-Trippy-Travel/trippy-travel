package com.trippyTravel.repositories;

import com.trippyTravel.models.Activity;
import com.trippyTravel.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentsByActivity_Id(long activityId);
    Comment findCommentByActivity_Id(long activityId);
    boolean existsCommentByActivity_Id(long activityId);
    List<Comment> findCommentsByTrip_Id(long tripId);
    List<Comment> findCommentsByTrip_IdOrderByCreatedAt(long tripId);

}
