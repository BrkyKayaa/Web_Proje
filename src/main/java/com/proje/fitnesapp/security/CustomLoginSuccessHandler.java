package com.proje.fitnesapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

/**
 * Kullanıcı başarılı bir şekilde giriş yaptığında yönlendirme işlemlerini yöneten handler sınıfıdır.
 * Kullanıcının rolüne göre farklı sayfalara yönlendirme yapar.
 */
@Component
@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Giriş yapan kullanıcının rolüne göre uygun sayfaya yönlendirme yapılır.
     *
     * @param request        HTTP isteği
     * @param response       HTTP yanıtı
     * @param authentication Kullanıcı kimlik doğrulaması
     * @throws IOException      I/O hatası
     * @throws ServletException Servlet hatası
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
                log.info("Admin giriş yaptı → yönlendiriliyor.");
                response.sendRedirect("/admin/dashboard");
                return;
            }

            if ("ROLE_USER".equalsIgnoreCase(role)) {
                log.info("Kullanıcı giriş yaptı → yönlendiriliyor.");
                response.sendRedirect("/profile");
                return;
            }
        }

        // Tanımsız veya desteklenmeyen rol varsa
        log.warn("Tanımsız rol ile giriş yapıldı.");
        response.sendRedirect("/auth/login?error");
    }
}
