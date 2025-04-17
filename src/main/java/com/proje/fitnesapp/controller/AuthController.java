package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.dto.UserRegisterDto;
import com.proje.fitnesapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Kullanıcı kimlik doğrulama işlemlerini (giriş, kayıt) yöneten controller.
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * Giriş ekranını gösterir.
     *
     * @return login sayfası
     */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    /**
     * Kayıt formunu gösterir.
     *
     * @param model thymeleaf modeli
     * @return register sayfası
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegisterDto());
        return "auth/register";
    }

    /**
     * Kayıt formu gönderildiğinde işlemleri yapar.
     *
     * @param dto    kayıt form verisi
     * @param result validasyon sonucu
     * @param model  thymeleaf modeli
     * @return kayıt başarılıysa login'e, aksi halde form tekrar gösterilir
     */
    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("user") UserRegisterDto dto,
                                  BindingResult result,
                                  Model model) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Şifreler eşleşmiyor");
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(dto);
            return "redirect:/auth/login?registerSuccess";
        } catch (Exception e) {
            model.addAttribute("error", "Kayıt sırasında bir hata oluştu: " + e.getMessage());
            return "auth/register";
        }
    }
}
