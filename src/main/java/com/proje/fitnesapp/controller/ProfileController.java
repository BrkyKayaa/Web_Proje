package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.dto.PasswordChangeDto;
import com.proje.fitnesapp.dto.UserUpdateDto;
import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.service.SubscriptionService;
import com.proje.fitnesapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

/**
 * Kullanıcı profil işlemlerini yöneten controller.
 * Profil görüntüleme, düzenleme ve şifre değiştirme gibi işlemleri içerir.
 */
@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    /**
     * Kullanıcının profil sayfasını gösterir.
     */
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("subscriptions", subscriptionService.getUserSubscriptions(user));
        return "user/profile";
    }

    /**
     * Giriş yapan kullanıcının profil fotoğrafını döner (image/jpeg).
     */
    @GetMapping("/profile/picture")
    @ResponseBody
    public ResponseEntity<byte[]> getOwnProfilePicture(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        byte[] image = user.getProfilePicture();

        if (image != null && image.length > 0) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Kullanıcının profil düzenleme sayfasını getirir.
     */
    @GetMapping("/profile/edit")
    public String editProfilePage(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());

        UserUpdateDto dto = new UserUpdateDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        model.addAttribute("userUpdateDto", dto);
        return "user/profile-edit";
    }

    /**
     * Profil güncelleme işlemini gerçekleştirir.
     */
    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute("userUpdateDto") UserUpdateDto dto,
                                Principal principal) throws IOException {
        userService.updateProfile(principal.getName(), dto);
        return "redirect:/profile?updated";
    }

    /**
     * Şifre değiştirme sayfasını gösterir.
     */
    @GetMapping("/profile/change-password")
    public String changePasswordPage(Model model) {
        model.addAttribute("passwordDto", new PasswordChangeDto());
        return "user/change-password";
    }

    /**
     * Şifre değiştirme işlemini gerçekleştirir.
     * Başarılı olursa kullanıcı oturumu sonlandırılır.
     */
    @PostMapping("/profile/change-password")
    public String changePassword(@ModelAttribute("passwordDto") PasswordChangeDto dto,
                                 Principal principal,
                                 Model model,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {

        boolean result = userService.changePassword(principal.getName(), dto);

        if (!result) {
            model.addAttribute("error", "Şifre güncellenemedi. Mevcut şifre hatalı veya yeni şifreler eşleşmiyor.");
            return "user/change-password";
        }

        // Şifre başarıyla değiştirildi → Otomatik logout
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/auth/login?passwordChanged";
    }
}
