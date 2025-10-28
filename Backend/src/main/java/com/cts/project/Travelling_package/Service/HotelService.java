package com.cts.project.Travelling_package.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Repository.HotelRepository;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;


    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }


    public Hotel getHotelById(Long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        return hotel.orElse(null);
    }


    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }


    public Hotel updateHotel(Long id, Hotel updatedHotel) {
        Optional<Hotel> existingHotel = hotelRepository.findById(id);
        if (existingHotel.isPresent()) {
            Hotel hotel = existingHotel.get();
            hotel.setName(updatedHotel.getName());
            hotel.setLocation(updatedHotel.getLocation());
            hotel.setRoomsAvailable(updatedHotel.getRoomsAvailable());
            hotel.setRating(updatedHotel.getRating());
            hotel.setPricePerNight(updatedHotel.getPricePerNight());
            return hotelRepository.save(hotel);
        } else {
            return null;
        }
    }


    public boolean deleteHotel(Long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if (hotel.isPresent()) {
            hotelRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


    public List<Hotel> getHotelsByLocation(String location) {
        return hotelRepository.findByLocation(location);
    }

}