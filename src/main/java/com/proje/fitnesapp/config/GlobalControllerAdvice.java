package com.proje.fitnesapp.config;

import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

/**
 * Tüm controller'lara global olarak model verisi sağlayan yapı.
 * Giriş yapan kullanıcı bilgisi thymeleaf gibi view'lara otomatik aktarılır.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserService userService;

    /**
     * Giriş yapmış olan kullanıcıyı view tarafına "currentUser" adıyla taşır.
     * Örneğin: <span th:text="${currentUser.username}">
     *
     * @param principal oturum açmış kullanıcının principal bilgisi
     * @return User objesi veya null
     */
    @ModelAttribute("currentUser")
    public User getCurrentUser(Principal principal) {
        return (principal != null) ? userService.findByEmail(principal.getName()) : null;
    }
}
