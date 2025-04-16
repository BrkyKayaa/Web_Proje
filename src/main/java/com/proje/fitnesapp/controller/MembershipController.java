package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.dto.MembershipDto;
import com.proje.fitnesapp.model.Membership;
import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/membership")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    // Listele
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

    // Ekleme formu
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("membershipDto", new MembershipDto());
        model.addAttribute("types", MembershipType.values());
        return "admin/admin-membership-add";
    }

    // Ekleme işlemi
    @PostMapping("/add")
    public String addMembership(@ModelAttribute MembershipDto dto) throws IOException {
        membershipService.create(dto);
        return "redirect:/admin/membership/list";
    }

    // Güncelleme formu
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Membership membership = membershipService.getById(id);
        model.addAttribute("membership", membership);
        model.addAttribute("types", MembershipType.values());
        return "admin/admin-membership-edit";
    }

    // Güncelleme işlemi
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

    // Silme işlemi
    @GetMapping("/delete/{id}")
    public String deleteMembership(@PathVariable Long id) {
        membershipService.delete(id);
        return "redirect:/admin/membership/list";
    }
}
