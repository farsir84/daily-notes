package com.dailynotes.config;

import com.dailynotes.entity.User;
import com.dailynotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //private final PasswordEncoder passwordEncoder;

    @Value("${app.default-user.username}")
    private String defaultUsername;

    @Value("${app.default-user.password}")
    private String defaultPassword;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername(defaultUsername)) {
            User user = new User();
            user.setUsername(defaultUsername);
            user.setPassword(passwordEncoder.encode(defaultPassword));
            userRepository.save(user);
            System.out.println("Default user created: " + defaultUsername);
        }
    }
}
