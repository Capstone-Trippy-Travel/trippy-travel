package com.trippyTravel.repositories;

import com.trippyTravel.models.FriendList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Fer on 1/10/17.
 */
@Repository
public interface FriendListRepository extends JpaRepository<FriendList, Long> {
    List<FriendList> findAllByStatus(String status);
    FriendList findFriendListByFriend_Id(Long id);
}
