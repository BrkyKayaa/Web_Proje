package com.proje.fitnesapp.service.impl;

import com.proje.fitnesapp.dto.PasswordChangeDto;
import com.proje.fitnesapp.dto.UserRegisterDto;
import com.proje.fitnesapp.dto.UserUpdateDto;
import com.proje.fitnesapp.model.Role;
import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.repository.UserRepository;
import com.proje.fitnesapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Kullanıcı kayıt, güncelleme ve parola işlemlerini yöneten servis katmanıdır.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Yeni kullanıcı kaydı oluşturur.
     *
     * @param dto kullanıcı kayıt bilgileri
     * @throws IOException görsel yükleme sırasında hata oluşursa
     */
    @Override
    public void register(UserRegisterDto dto) throws IOException {
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .profilePicture(convertToBytes(dto.getProfilePicture()))
                .build();

        userRepository.save(user);
    }

    /**
     * E-posta adresine göre kullanıcıyı döner.
     *
     * @param email kullanıcı e-posta
     * @return bulunan kullanıcı veya null
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * ID'ye göre kullanıcıyı getirir.
     *
     * @param id kullanıcı ID'si
     * @return bulunan kullanıcı veya null
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Kullanıcının profil bilgilerini günceller.
     *
     * @param email e-posta adresi
     * @param dto   yeni kullanıcı bilgileri
     * @throws IOException görsel yüklenemediğinde
     */
    @Override
    public void updateProfile(String email, UserUpdateDto dto) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + email));

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        if (dto.getProfilePicture() != null && !dto.getProfilePicture().isEmpty()) {
            user.setProfilePicture(convertToBytes(dto.getProfilePicture()));
        }

        userRepository.save(user);
    }

    /**
     * Kullanıcının şifresini değiştirir.
     *
     * @param email kullanıcının e-postası
     * @param dto   parola değiştirme bilgileri
     * @return işlem başarılıysa true, aksi halde false
     */
    @Override
    public boolean changePassword(String email, PasswordChangeDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + email));

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

    /**
     * MultipartFile nesnesini byte dizisine dönüştürür.
     *
     * @param file resim dosyası
     * @return byte[] formatında dosya
     * @throws IOException dönüşüm sırasında hata oluşursa
     */
    private byte[] convertToBytes(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            return file.getBytes();
        }
        return null;
    }
}
