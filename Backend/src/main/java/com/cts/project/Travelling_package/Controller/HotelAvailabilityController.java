package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Model.HotelAvailability;
import com.cts.project.Travelling_package.Service.HotelAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hotel-availability")
public class HotelAvailabilityController {

    @Autowired
    private HotelAvailabilityService service;

    @GetMapping
    public List<HotelAvailability> getAvailableHotels(
            @RequestParam String location,
            @RequestParam String date) {

        LocalDate localDate = LocalDate.parse(date);
        return service.getAvailableHotels(location, localDate);
    }


    @PostMapping("/create")
    public HotelAvailability createHotelAvailability(@RequestBody HotelAvailability hotelAvailability) {
        return service.createHotelAvailability(hotelAvailability);
    }


    @GetMapping("/{id}")
    public HotelAvailability getHotelAvailability(@PathVariable Long id) {

        return service.getHotelAvailability(id);
    }


    @PutMapping("/update/{id}")
    public HotelAvailability updateHotelAvailability(
            @PathVariable Long id,
            @RequestBody HotelAvailability hotelAvailability) {
        return service.updateHotelAvailability(id, hotelAvailability);
    }


    @DeleteMapping("/delete/{id}")
    public void deleteHotelAvailability(@PathVariable Long id) {
        service.deleteHotelAvailability(id);
    }


    @GetMapping("/all")
    public List<HotelAvailability> getAllHotelAvailability() {
        return service.getAllHotelAvailability();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }
}
