package com.trippyTravel.repositories;

import com.trippyTravel.models.Group;
import com.trippyTravel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Group, Long> {
    List<Group> findByOwner(User owner);

}
