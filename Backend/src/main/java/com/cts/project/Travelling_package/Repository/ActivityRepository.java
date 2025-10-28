package com.cts.project.Travelling_package.Repository;

import com.cts.project.Travelling_package.Model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Package.Activity,Long> {
    List<Package.Activity> findByLocation(String location);
}