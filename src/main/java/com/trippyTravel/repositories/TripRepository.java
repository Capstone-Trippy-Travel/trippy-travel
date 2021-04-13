package com.trippyTravel.repositories;

import com.trippyTravel.models.Trips;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trips,Long> {
}
