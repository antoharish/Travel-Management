package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.User;
import com.cts.project.Travelling_package.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    List<User> getAllUsers();

    Optional<User> getUserById(int id);

    boolean createUser(User user);

    User updateUser(int id, User user);

    void deleteUser(int id);

    String authenticateUser(String username, String rawPassword);

    boolean createHotelManager(User user);

    boolean createTravelAgent(User user);



    User findById(Long userId);
}