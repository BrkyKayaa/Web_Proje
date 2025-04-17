package com.proje.fitnesapp.service.impl;

import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Spring Security için kullanıcı doğrulamasını sağlayan servis katmanıdır.
 * Sisteme giriş yapan kullanıcıyı email ile doğrular.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Verilen e-posta adresine göre kullanıcıyı veritabanından bulur ve Spring Security'e uygun bir User nesnesi döner.
     *
     * @param email giriş yapan kullanıcının e-posta adresi
     * @return UserDetails → Spring Security'nin tanıdığı kullanıcı objesi
     * @throws UsernameNotFoundException kullanıcı bulunamazsa
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                .build();
    }
}
