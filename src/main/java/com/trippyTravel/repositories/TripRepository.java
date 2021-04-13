package com.trippyTravel.repositories;

import com.trippyTravel.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip,Long> {
}
