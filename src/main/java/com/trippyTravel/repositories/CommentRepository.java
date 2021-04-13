package com.trippyTravel.repositories;

import com.trippyTravel.models.comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<comment,Long> {
}
