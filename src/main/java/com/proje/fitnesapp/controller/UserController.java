package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.service.AdminUserFilterService;
import com.proje.fitnesapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private AdminUserFilterService adminUserFilterService;
    @Autowired
    private UserService userService;

    @GetMapping("/admin/user/profile/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProfileImageById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user.getProfilePicture() != null) {
            byte[] image = new byte[user.getProfilePicture().length];
            for (int i = 0; i < image.length; i++) image[i] = user.getProfilePicture()[i];
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        }
        return ResponseEntity.notFound().build();
    }


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
            }
        } else {
            model.addAttribute("users", adminUserFilterService.getAllUsers());
        }

        return "admin/admin-user-list";
    }
}
