package com.cts.project.Travelling_package.Repository;

import com.cts.project.Travelling_package.Model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByLocation(String location);
}
