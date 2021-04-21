package com.trippyTravel.repositories;

import com.trippyTravel.models.Trip;
import com.trippyTravel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip,Long> {

    @Query("select t from Trip t, Group g, GroupMember gm where t.group=g AND gm.unreadCommentTrip=t AND gm.member=?1")
    List<Trip> getUnreadCommentTrips(User user);
}
