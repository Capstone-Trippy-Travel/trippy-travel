package com.trippyTravel.repositories;

import com.trippyTravel.models.GroupMember;
import com.trippyTravel.models.Trip;
import com.trippyTravel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMembersRepository extends JpaRepository<GroupMember, Long> {
    List <GroupMember> findGroupMembersByMember(User member);
    Boolean existsGroupMemberByMemberAndUnreadCommentTripAndUnreadComment(User user, Trip trip, Boolean existsUnreadComments);
    GroupMember findGroupMemberByMemberAndUnreadCommentTripAndUnreadComment(User user, Trip trip, Boolean existsUnreadComments);

}
