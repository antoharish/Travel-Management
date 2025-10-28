package com.cts.project.Travelling_package.Repository;
import com.cts.project.Travelling_package.Model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary,Long> {
    Optional<Itinerary> findById(Long itineraryId);


}