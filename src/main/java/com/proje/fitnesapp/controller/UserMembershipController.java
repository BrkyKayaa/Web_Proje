package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.model.Membership;
import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserMembershipController {

    @Autowired
    private MembershipService membershipService;

    // Ana sayfa: tüm üyelikleri veya filtrelenmiş olanları getirir
    @GetMapping("/")
    public String showAllMemberships(@RequestParam(value = "type", required = false) String typeParam, Model model) {
        List<Membership> memberships;

        if (typeParam != null) {
            try {
                MembershipType type = MembershipType.valueOf(typeParam);
                memberships = membershipService.getByType(type);
                model.addAttribute("selectedType", type.name()); // seçilen enum string
            } catch (IllegalArgumentException e) {
                memberships = membershipService.getAll();
                model.addAttribute("selectedType", null); // hatalıysa sıfırla
            }
        } else {
            memberships = membershipService.getAll();
            model.addAttribute("selectedType", null);
        }

        model.addAttribute("memberships", memberships);
        model.addAttribute("types", MembershipType.values()); // enum listesi

        return "user/index";
    }

    // Detay sayfası: tek üyelik bilgisi
    @GetMapping("/membership/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("membership", membershipService.getById(id));
        return "user/membership-details";
    }

    // Görseli göster: üyelik resmini response olarak döner
    @GetMapping("/membership/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getMembershipImage(@PathVariable Long id) {
        Membership membership = membershipService.getById(id);
        if (membership.getImage() != null) {
            byte[] imageBytes = new byte[membership.getImage().length];
            for (int i = 0; i < imageBytes.length; i++) imageBytes[i] = membership.getImage()[i];
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        }
        return ResponseEntity.notFound().build();
    }
}