/*
package com.cts.project.Travelling_package.Repository;

import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Model.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {


    Optional<Hotel> findByHotel_Id(Long hotelId) ;
}
*/
package com.cts.project.Travelling_package.Repository;

import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Model.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {

    Optional<Hotel> findByHotel_HotelId(Long hotelId); // Updated to use 'HotelId'
}