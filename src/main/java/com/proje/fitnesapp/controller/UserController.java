package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.service.AdminUserFilterService;
import com.proje.fitnesapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Admin panelinde kullanıcıları listeleme ve profil görsellerini yönetme işlemlerini sağlayan controller.
 */
@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final AdminUserFilterService adminUserFilterService;
    private final UserService userService;

    /**
     * Belirtilen ID'ye sahip kullanıcının profil görselini döner.
     *
     * @param id Kullanıcı ID'si
     * @return JPEG formatında profil resmi veya 404
     */
    @GetMapping("/profile/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProfileImageById(@PathVariable Long id) {
        User user = userService.findById(id);

        byte[] image = user.getProfilePicture();
        if (image != null && image.length > 0) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Kullanıcı listesini döner. Opsiyonel olarak üyelik türüne göre filtreleme yapılabilir.
     *
     * @param typeParam Opsiyonel üyelik tipi (enum adı)
     * @param model     Thymeleaf modeli
     * @return Kullanıcı listeleme sayfası
     */
    @GetMapping("/list")
    public String listUsers(@RequestParam(value = "type", required = false) String typeParam, Model model) {
        model.addAttribute("types", MembershipType.values());

        if (typeParam != null) {
            try {
                MembershipType type = MembershipType.valueOf(typeParam);
                model.addAttribute("users", adminUserFilterService.getUsersByMembershipType(type));
                model.addAttribute("selectedType", type.name());
            } catch (IllegalArgumentException e) {
                model.addAttribute("users", adminUserFilterService.getAllUsers());
                model.addAttribute("selectedType", null);
            }
        } else {
            model.addAttribute("users", adminUserFilterService.getAllUsers());
            model.addAttribute("selectedType", null);
        }

        return "admin/admin-user-list";
    }
}
