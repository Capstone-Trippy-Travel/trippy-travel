package com.trippyTravel.repositories;

import com.trippyTravel.models.TripLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripLikeRepository extends JpaRepository<TripLikes, Long> {
}
