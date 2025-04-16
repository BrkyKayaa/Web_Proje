package com.proje.fitnesapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String username;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    private String password;

    @NotBlank(message = "Şifre tekrar boş olamaz")
    private String confirmPassword;

    @Getter
    @Setter
    private MultipartFile profilePicture;

    public @NotBlank(message = "Kullanıcı adı boş olamaz") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Kullanıcı adı boş olamaz") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Email boş olamaz") @Email(message = "Geçerli bir email adresi giriniz") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email boş olamaz") @Email(message = "Geçerli bir email adresi giriniz") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Şifre boş olamaz") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Şifre boş olamaz") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Şifre tekrar boş olamaz") String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank(message = "Şifre tekrar boş olamaz") String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }
}
