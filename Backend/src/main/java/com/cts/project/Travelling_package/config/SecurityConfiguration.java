package com.cts.project.Travelling_package.config;

import com.cts.project.Travelling_package.Service.CustomerUserDetailsService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                . cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll()
///                      .requestMatchers("/api/register","/api/login","/api/payments/success/**").permitAll()
//                        .requestMatchers(
//                                "/api/review/user/**",
//                                "/api/tickets",
//                                "/api/payments/process",
//                                "/pg/user","/tm/searches","/itineraries"
//                        ).hasRole("USER")
//                        .requestMatchers("/hotels/user").hasAnyRole("USER","ADMIN")
//                        .requestMatchers("/api/review/**","/api/hotels").hasRole("HOTELMANAGER")
//                        .requestMatchers("/api/**").hasRole("ADMIN")
////                        .requestMatchers("/pg/tm/**").hasRole("TRAVELAGENT")
//                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("anto")
//                .password("1234")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);

        return new CustomerUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider =new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
