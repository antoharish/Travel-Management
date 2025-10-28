package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Model.HotelAvailability;
import com.cts.project.Travelling_package.Repository.HotelAvailabilityRepository;
import com.cts.project.Travelling_package.Repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HotelAvailabilityService {

    private final HotelRepository hotelRepository;
    private final HotelAvailabilityRepository availabilityRepository;

    @Autowired
    public HotelAvailabilityService(HotelRepository hotelRepository,
                                    HotelAvailabilityRepository availabilityRepository) {
        this.hotelRepository = hotelRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public List<HotelAvailability> getAvailableHotels(String location, LocalDate date) {

        List<Hotel> hotelsInLocation = hotelRepository.findByLocation(location);
        List<HotelAvailability> results = new ArrayList<>();

        for (Hotel hotel : hotelsInLocation) {

            Optional<HotelAvailability> optionalAvailability =
                    availabilityRepository.findByHotel_HotelIdAndDate(hotel.getHotelId(), date);

            HotelAvailability availability;

            if (optionalAvailability.isPresent()) {
                availability = optionalAvailability.get();
            } else {

                availability = new HotelAvailability();
                availability.setHotel(hotel);
                availability.setDate(date);
                availability.setCapacity(hotel.getRoomsAvailable());
                availability = availabilityRepository.save(availability);
            }


            if (availability.getCapacity() > 0) {
                results.add(availability);
            }
        }

        return results;
    }



    public HotelAvailability createHotelAvailability(HotelAvailability hotelAvailability) {
        return availabilityRepository.save(hotelAvailability);
    }

    public HotelAvailability getHotelAvailability(Long id) {
        return availabilityRepository.findById(id).orElse(null);
    }

    public HotelAvailability updateHotelAvailability(Long id, HotelAvailability hotelAvailability) {
        return availabilityRepository.findById(id).map(existing -> {
            existing.setDate(hotelAvailability.getDate());
            existing.setCapacity(hotelAvailability.getCapacity());
            existing.setHotel(hotelAvailability.getHotel());
            return availabilityRepository.save(existing);
        }).orElse(null);
    }

    public void deleteHotelAvailability(Long id) {
        availabilityRepository.deleteById(id);
    }

    public List<HotelAvailability> getAllHotelAvailability() {
        return availabilityRepository.findAll();
    }

    public void deletePastHotelAvailability() {
        availabilityRepository.findByDateBefore(LocalDate.now())
                .forEach(availabilityRepository::delete);
    }
}
