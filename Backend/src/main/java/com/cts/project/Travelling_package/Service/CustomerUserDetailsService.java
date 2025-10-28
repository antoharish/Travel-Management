package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.User;

import com.cts.project.Travelling_package.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired

    private UserRepository userRepository;

    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username) .orElseThrow( () -> new UsernameNotFoundException("No UserName Found"));

        return new org.springframework.security.core.userdetails.User(

                user.getUsername(),

                user.getPassword(),

                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))

        );

    }

}

