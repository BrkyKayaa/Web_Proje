package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.service.SubscriptionService;
import com.proje.fitnesapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

/**
 * Üyelik satın alma işlemlerini yöneten controller.
 * Kullanıcı bir üyelik satın aldığında burada işlenir.
 */
@Controller
@RequestMapping("/membership")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    /**
     * Kullanıcının bir üyeliği satın alma işlemini gerçekleştirir.
     *
     * @param id üyelik ID'si
     * @param principal giriş yapan kullanıcı bilgisi
     * @return Satın alma başarılıysa onay sayfasına yönlendirir, aksi halde detay sayfasına hata mesajıyla döner
     */
    @PostMapping("/purchase/{id}")
    public String purchaseMembership(@PathVariable Long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());

        try {
            subscriptionService.createSubscription(user, id);
            return "redirect:/membership/purchase-confirmation";
        } catch (IllegalStateException e) {
            String errorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/membership/details/" + id + "?error=" + errorMessage;
        }
    }

    /**
     * Üyelik satın alma işlemi başarılıysa gösterilen onay sayfası.
     */
    @GetMapping("/purchase-confirmation")
    public String showPurchaseConfirmation() {
        return "user/purchase-confirmation";
    }
}
