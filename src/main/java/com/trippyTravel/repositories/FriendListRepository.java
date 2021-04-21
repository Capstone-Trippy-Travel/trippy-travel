package com.trippyTravel.repositories;

import com.trippyTravel.models.FriendList;
import com.trippyTravel.models.FriendStatus;
import com.trippyTravel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fer on 1/10/17.
 */
@Repository
public interface FriendListRepository extends JpaRepository<FriendList, Long> {
    List<FriendList> findAllByStatus(String status);
    FriendList findFriendListByFriend_IdAndUser_Id(Long friendId, long userId);
    Boolean existsFriendListByFriend_IdAndUser_Id(Long friendId, long userId);
    List<FriendList> findFriendListByFriendAndStatus(User friend, FriendStatus status);
}
