package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.dto.MembershipDto;
import com.proje.fitnesapp.model.Membership;
import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Admin tarafından üyelik paketlerini yönetmek için kullanılan controller sınıfı.
 */
@Controller
@RequestMapping("/admin/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    /**
     * Üyelik listesi sayfası.
     * Opsiyonel olarak MembershipType ile filtreleme yapılabilir.
     */
    @GetMapping("/list")
    public String listMemberships(@RequestParam(value = "type", required = false) String typeParam, Model model) {
        List<Membership> memberships;

        if (typeParam != null) {
            try {
                MembershipType type = MembershipType.valueOf(typeParam);
                memberships = membershipService.getByType(type);
                model.addAttribute("selectedType", type.name());
            } catch (IllegalArgumentException e) {
                memberships = membershipService.getAll();
                model.addAttribute("selectedType", null);
            }
        } else {
            memberships = membershipService.getAll();
            model.addAttribute("selectedType", null);
        }

        model.addAttribute("memberships", memberships);
        model.addAttribute("types", MembershipType.values());
        return "admin/admin-membership-list";
    }

    /**
     * Yeni üyelik ekleme formu.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("membershipDto", new MembershipDto());
        model.addAttribute("types", MembershipType.values());
        return "admin/admin-membership-add";
    }

    /**
     * Yeni üyelik ekleme işlemi.
     */
    @PostMapping("/add")
    public String addMembership(@Valid @ModelAttribute("membershipDto") MembershipDto dto, BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {                                // ② kontrol et
            model.addAttribute("types", MembershipType.values()); // formda select kutusu lazımdı
            return "admin/admin-membership-add";                 // ③ hatayla forma geri dön
        }

        membershipService.create(dto);
        return "redirect:/admin/membership/list";
    }

    /**
     * Üyelik güncelleme formu.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Membership membership = membershipService.getById(id);
        if (membership == null) {
            return "redirect:/admin/membership/list";
        }

        model.addAttribute("membership", membership);
        model.addAttribute("types", MembershipType.values());
        return "admin/admin-membership-edit";
    }

    /**
     * Üyelik güncelleme işlemi.
     */
    @PostMapping("/edit/{id}")
    public String editMembership(@PathVariable Long id,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam Double price,
                                 @RequestParam Integer durationInDays,
                                 @RequestParam MembershipType type,
                                 @RequestParam(required = false) MultipartFile imageFile) throws IOException {

        membershipService.updateMembership(id, title, description, price, durationInDays, type, imageFile);
        return "redirect:/admin/membership/list";
    }

    /**
     * Üyelik silme işlemi.
     */
    @GetMapping("/delete/{id}")
    public String deleteMembership(@PathVariable Long id) {
        membershipService.delete(id);
        return "redirect:/admin/membership/list";
    }
}
