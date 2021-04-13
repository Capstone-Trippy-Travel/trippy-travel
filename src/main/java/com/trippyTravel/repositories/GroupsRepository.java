package com.trippyTravel.repositories;

import com.trippyTravel.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsRepository extends JpaRepository<Group, Long> {
}
