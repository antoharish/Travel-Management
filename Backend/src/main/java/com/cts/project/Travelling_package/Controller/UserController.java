package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Model.User;

import com.cts.project.Travelling_package.Repository.UserRepository;
import com.cts.project.Travelling_package.Service.ServiceImpl.UserServiceImpl;

import com.cts.project.Travelling_package.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Map;

import java.util.Optional;

@RestController

@RequestMapping("/api")

public class UserController {

    @Autowired

    private UserService userService;

    @Autowired

    private UserServiceImpl userServiceImpl;

    @Autowired

    private PasswordEncoder passwordEncoder;

    @GetMapping

    public List<User> getAllUsers() {

        return userService.getAllUsers();

    }

    @GetMapping("/{id}")

    public Optional<User> getUserById(@PathVariable int id) {

        Optional<User> user = userService.getUserById(id);

        return user;

    }

//    @PostMapping

//    public User createUser(@RequestBody User user) {

//        user.setPassword(passwordEncoder.encode(user.getPassword()));

//        return userService.createUser(user);

//    }

    @PostMapping("/register")

    public ResponseEntity<?> registerUser(@RequestBody User user) {

        boolean isRegister = userServiceImpl.createUser(user);

        if (isRegister) {

            return ResponseEntity.ok(Map.of("success", true, "message", "User registered successfully"));

        } else {

            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Registration failed. Please try again."));

        }

    }

    @PostMapping("/register/hm")

    public ResponseEntity<?> registerHotelManager(@RequestBody User user) {

        boolean isRegister = userServiceImpl.createHotelManager(user);

        if (isRegister) {

            return ResponseEntity.ok(Map.of("success", true, "message", "Hotel-Manager registered successfully"));

        } else {

            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Registration failed. Please try again."));

        }

    }

    @PostMapping("/register/tm")

    public ResponseEntity<?> registerTravelManager(@RequestBody User user) {

        boolean isRegister = userServiceImpl.createTravelAgent(user);

        if (isRegister) {

            return ResponseEntity.ok(Map.of("success", true, "message", "Travel-Agent registered successfully"));

        } else {

            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Registration failed. Please try again."));

        }

    }

    //LOGIN

    @PostMapping("/login")

    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {

        String username = loginRequest.get("username");

        String password = loginRequest.get("password");

        if (username == null || password == null) {

            return ResponseEntity.badRequest().body("Email and password are required.");

        }

        String token = userServiceImpl.authenticateUser(username, password);

        if (token != null) {

            return ResponseEntity.ok(Map.of("token", token)); // Return the JWT token

        } else {

            return ResponseEntity.status(401).body("Invalid username or password.");

        }

    }

    @PutMapping("/{id}")

    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User userDetails) {

        User updatedUser = userService.updateUser(id, userDetails);

        return ResponseEntity.ok(updatedUser);

    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteUser(@PathVariable int id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(
            @PathVariable int id,
            @RequestBody Map<String, String> passwordMap) {

        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");

        Optional<User> optionalUser = userService.getUserById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = optionalUser.get();

        // Check old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is incorrect.");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(id, user);

        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }



}
