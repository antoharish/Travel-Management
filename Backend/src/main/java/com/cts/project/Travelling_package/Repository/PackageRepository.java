package com.cts.project.Travelling_package.Repository;

import com.cts.project.Travelling_package.Model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findByLocationAndStartDateBetweenAndNoOfPeopleGreaterThanEqual(
            String location, LocalDate startDate, LocalDate endDate, int noOfPeople);


}
