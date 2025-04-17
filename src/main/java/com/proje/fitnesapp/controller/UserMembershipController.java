package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.model.Membership;
import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kullanıcı tarafında üyelikleri listeleme, detaylarını gösterme ve görselini sunma işlemlerini yöneten controller.
 */
@Controller
@RequiredArgsConstructor
public class UserMembershipController {

    private final MembershipService membershipService;

    /**
     * Ana sayfa – tüm üyelikleri listeler. Opsiyonel olarak MembershipType ile filtrelenebilir.
     */
    @GetMapping("/")
    public String showAllMemberships(@RequestParam(value = "type", required = false) String typeParam, Model model) {
        List<Membership> memberships;
        String selectedType = null;

        if (typeParam != null) {
            try {
                MembershipType type = MembershipType.valueOf(typeParam);
                memberships = membershipService.getByType(type);
                selectedType = type.name();
            } catch (IllegalArgumentException e) {
                memberships = membershipService.getAll();
            }
        } else {
            memberships = membershipService.getAll();
        }

        model.addAttribute("memberships", memberships);
        model.addAttribute("types", MembershipType.values());
        model.addAttribute("selectedType", selectedType);

        return "user/index";
    }

    /**
     * Belirli bir üyeliğin detay sayfasını gösterir.
     */
    @GetMapping("/membership/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        Membership membership = membershipService.getById(id);
        if (membership == null) {
            return "redirect:/?error=notfound";
        }
        model.addAttribute("membership", membership);
        return "user/membership-details";
    }

    /**
     * Belirli bir üyeliğe ait görseli döner (image/jpeg).
     */
    @GetMapping("/membership/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getMembershipImage(@PathVariable Long id) {
        Membership membership = membershipService.getById(id);
        byte[] imageBytes = membership.getImage();

        if (imageBytes != null && imageBytes.length > 0) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }


        return ResponseEntity.notFound().build();
    }
}
