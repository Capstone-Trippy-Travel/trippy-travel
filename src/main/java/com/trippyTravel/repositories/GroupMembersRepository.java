package com.trippyTravel.repositories;

import com.trippyTravel.models.GroupMembers;
import com.trippyTravel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMembersRepository extends JpaRepository<GroupMembers, Long> {

}
