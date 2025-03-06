package com.example.capstone.runner;


import com.example.capstone.enumaration.UserRole;
import com.example.capstone.model.Role;
import com.example.capstone.model.User;
import com.example.capstone.repository.RoleRepository;
import com.example.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    RoleRepository repository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (repository.count() == 0 && userRepository.count()==0) {

            Role userRole = new Role();
            userRole.setRole(UserRole.USER);
            repository.save(userRole);

            Role adminRole = new Role();
            adminRole.setRole(UserRole.ADMIN);
            repository.save(adminRole);

            Set<Role> roles=new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);

            User admin01=new User().builder().name("Mario").roles(roles).email("admin@example.com").surname("Rossi")
                    .username("admin01").password(passwordEncoder.encode("123456")).build();


            userRepository.save(admin01);

        }


    }
}
