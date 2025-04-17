package com.proje.fitnesapp.dto;

import com.proje.fitnesapp.model.MembershipType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * Yeni bir üyelik oluşturmak veya güncellemek için kullanılan DTO sınıfı.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDto {

    @NotBlank(message = "Başlık boş olamaz")
    private String title;

    @NotBlank(message = "Açıklama boş olamaz")
    private String description;

    @NotNull(message = "Fiyat boş olamaz")
    @DecimalMin(value = "0.0", inclusive = false, message = "Fiyat sıfırdan büyük olmalı")
    private Double price;

    @NotNull(message = "Süre boş olamaz")
    @Min(value = 1, message = "Süre en az 1 gün olmalı")
    private Integer durationInDays;

    @NotNull(message = "Üyelik tipi boş olamaz")
    private MembershipType type;

    private MultipartFile imageFile;
}
