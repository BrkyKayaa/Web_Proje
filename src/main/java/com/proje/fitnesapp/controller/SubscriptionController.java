package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.service.SubscriptionService;
import com.proje.fitnesapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/membership")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;

    @PostMapping("/purchase/{id}")
    public String purchaseMembership(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());

        try {
            subscriptionService.createSubscription(user, id);
            return "redirect:/membership/purchase-confirmation";
        } catch (IllegalStateException e) {
            // Hata varsa detay sayfasına geri dönüp mesaj göster
            return "redirect:/membership/details/" + id + "?error=" + e.getMessage();
        }
    }


    @GetMapping("/purchase-confirmation")
    public String showPurchaseConfirmation(Model model) {
        return "user/purchase-confirmation";
    }
}
