package com.trippyTravel.repositories;

import com.trippyTravel.models.ActivityVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityVoteRepository extends JpaRepository<ActivityVote,Long> {
    ActivityVote findByActivity_Id(Long activity_id);
}
