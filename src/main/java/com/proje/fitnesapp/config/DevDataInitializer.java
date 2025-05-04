package com.proje.fitnesapp.config;

import com.proje.fitnesapp.model.Role;
import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev") // sadece development ortamında çalışır
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.admin.username}")
    private String adminUsername;

    @Value("${default.admin.email}")
    private String adminEmail;

    @Value("${default.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println("Varsayılan admin oluşturuldu: " + adminEmail);
        } else {
            System.out.println("Admin zaten mevcut: " + adminEmail);
        }
    }
}
