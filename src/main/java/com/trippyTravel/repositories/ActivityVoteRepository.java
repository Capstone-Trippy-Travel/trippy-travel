package com.trippyTravel.repositories;

import com.trippyTravel.models.Activity;
import com.trippyTravel.models.ActivityVote;
import com.trippyTravel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityVoteRepository extends JpaRepository<ActivityVote,Long> {
    ActivityVote findByActivity_Id(Long activity_id);
    List<ActivityVote> findActivityVotesByActivity(Activity activity);
    Boolean existsActivityVoteByActivityAndUser(Activity activity, User user);
    ActivityVote findActivityVoteByActivityAndUser(Activity activity, User user);
}
