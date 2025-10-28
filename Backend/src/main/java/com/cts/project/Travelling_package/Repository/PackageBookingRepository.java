package com.cts.project.Travelling_package.Repository;

import com.cts.project.Travelling_package.Model.PackageBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageBookingRepository extends JpaRepository<PackageBooking,Long> {
//    Optional<PackageBooking> findByAPackagePackageId(Long packageId);

}
