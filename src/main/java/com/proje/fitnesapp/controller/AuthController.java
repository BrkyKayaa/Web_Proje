package com.proje.fitnesapp.controller;

import com.proje.fitnesapp.dto.UserRegisterDto;
import com.proje.fitnesapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDto", new UserRegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("userDto") UserRegisterDto dto,
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
            model.addAttribute("error", "Kayıt sırasında bir hata oluştu.");
            return "auth/register";
        }
    }
}


