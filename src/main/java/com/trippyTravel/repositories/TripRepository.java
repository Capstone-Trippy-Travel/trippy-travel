package com.trippyTravel.repositories;

import com.trippyTravel.models.Trip;
import com.trippyTravel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip,Long> {


    @Query("select t from Trip t, Group g, GroupMember gm, User u where t.group=g AND gm.group=g AND gm.member=u AND u.id=?1 AND t.status=?2")
    List<Trip> findTripsByStatus(long id, String string);


//    @Query("select t from Trip t, Group g, GroupMember gm where t.group=g AND gm.group=gm AND gm.member=?1 AND t.status=?2")

//    @Query("select t from Trip t, Group g, GroupMember gm where t.group=g AND gm.member=?1 AND t.status=?2")
//    List<Trip> findTripsByStatus(User user, String string);


    @Query("select t from Trip t, Group g, GroupMember gm where t.group=g AND gm.unreadCommentTrip=t AND gm.member=?1")
    List<Trip> getUnreadCommentTrips(User user);

    @Query("from Trip trip where trip.description like %:term%")
    List<Trip> searchTrip(@Param("term") String term);

    @Query("from Trip trip where trip.name like %:term%")
    List<Trip> findByDescriptionContainingOrNameContainingOrLocationContaining(@Param("term") String term, @Param("term") String term1, @Param("term") String term2);
}
