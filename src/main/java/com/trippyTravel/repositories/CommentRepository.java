package com.trippyTravel.repositories;

import com.trippyTravel.models.comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<comment,Long> {
}
