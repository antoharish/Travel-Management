package com.cts.project.Travelling_package.Service.ServiceImpl;

import com.cts.project.Travelling_package.Model.User;
import com.cts.project.Travelling_package.Repository.UserRepository;
import com.cts.project.Travelling_package.Security.JwtUtil;
import com.cts.project.Travelling_package.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean createUser(User user) {
        try {
            // Check if the username already exists
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                System.out.println("User registration failed - Username already exists: " + user.getUsername());
                return false; // Username already exists
            }

            // Encode password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER");
            userRepository.save(user);
            System.out.println("User registration successful: " + user.getUsername());
            return true;

        } catch (Exception e) {
            e.printStackTrace(); // Prints the full error stack trace
            return false;
        }
    }

    @Override
    public boolean createHotelManager(User user) {
        try {
            // Check if the username already exists
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                System.out.println("HotelManager registration failed - Username already exists: " + user.getUsername());
                return false; // Username already exists
            }

            // Encode password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("HOTELMANAGER");
            userRepository.save(user);
            System.out.println("Hotel Manager registration successful: " + user.getUsername());
            return true;

        } catch (Exception e) {
            e.printStackTrace(); // Prints the full error stack trace
            return false;
        }
    }

    @Override
    public boolean createTravelAgent(User user) {
        try {
            // Check if the username already exists
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                System.out.println("TravelAgent failed - Username already exists: " + user.getUsername());
                return false; // Username already exists
            }

            // Encode password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("TRAVELAGENT");

            // Save the user
            userRepository.save(user);
            System.out.println("Travel Agent registration successful: " + user.getUsername());
            return true;

        } catch (Exception e) {
            e.printStackTrace(); // Prints the full error stack trace
            return false;
        }
    }

    @Override
    public User findById(Long userId) {
        return null;
    }

    @Override
    public String authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            System.out.println("No user found with username: " + username);
            return null;
        }
        User user = userOpt.get();
        // Compare the raw password with the stored encoded password
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getUserId());
        }
        System.out.println("Invalid password for username: " + username);
        return null;
    }


    @Override
    public User updateUser(int id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
