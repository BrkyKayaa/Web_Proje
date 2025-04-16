package com.proje.fitnesapp.service.impl;

import com.proje.fitnesapp.dto.PasswordChangeDto;
import com.proje.fitnesapp.dto.UserRegisterDto;
import com.proje.fitnesapp.dto.UserUpdateDto;
import com.proje.fitnesapp.model.Role;
import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.repository.UserRepository;
import com.proje.fitnesapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(UserRegisterDto dto) throws IOException {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);

        // FOTOĞRAF YÜKLEME BLOĞU
        MultipartFile file = dto.getProfilePicture();
        if (file != null && !file.isEmpty()) {
            System.out.println("📷 Gelen dosya: " + file.getOriginalFilename());

            byte[] bytes = file.getBytes();
            Byte[] byteObjects = new Byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                byteObjects[i] = bytes[i];
            }
            user.setProfilePicture(byteObjects);
        } else {
            System.out.println("❌ Kullanıcı fotoğraf yüklemedi veya dosya null.");
        }

        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void updateProfile(String email, UserUpdateDto dto) throws IOException {
        User user = userRepository.findByEmail(email).orElseThrow();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        MultipartFile file = dto.getProfilePicture();
        if (file != null && !file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Byte[] byteObjects = new Byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) byteObjects[i] = bytes[i];
            user.setProfilePicture(byteObjects);
        }

        userRepository.save(user);
    }

    @Override
    public boolean changePassword(String email, PasswordChangeDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            return false;
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return true;
    }

}

