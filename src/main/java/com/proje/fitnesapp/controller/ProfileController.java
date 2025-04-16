package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.dto.PasswordChangeDto;
import com.proje.fitnesapp.dto.UserUpdateDto;
import com.proje.fitnesapp.model.User;
import org.springframework.ui.Model;
import com.proje.fitnesapp.service.SubscriptionService;
import com.proje.fitnesapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.io.IOException;
import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("subscriptions", subscriptionService.getUserSubscriptions(user));
        return "user/profile";
    }
    @GetMapping("/profile/picture")
    @ResponseBody
    public ResponseEntity<byte[]> getOwnProfilePicture(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user.getProfilePicture() != null) {
            byte[] image = new byte[user.getProfilePicture().length];
            for (int i = 0; i < image.length; i++) image[i] = user.getProfilePicture()[i];
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/profile/edit")
    public String editProfilePage(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        UserUpdateDto dto = new UserUpdateDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        model.addAttribute("userUpdateDto", dto);
        return "user/profile-edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute("userUpdateDto") UserUpdateDto dto,
                                Principal principal) throws IOException {
        userService.updateProfile(principal.getName(), dto);
        return "redirect:/profile?updated";
    }
    @GetMapping("/profile/change-password")
    public String changePasswordPage(Model model) {
        model.addAttribute("passwordDto", new PasswordChangeDto());
        return "user/change-password";
    }

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

        // Şifre başarıyla değiştiğinde otomatik logout işlemi
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/auth/login?passwordChanged";
    }


}

