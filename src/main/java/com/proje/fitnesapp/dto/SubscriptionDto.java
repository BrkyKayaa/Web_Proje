package com.proje.fitnesapp.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    private Long userId;
    private Long membershipId;
    private LocalDate startDate;
    private LocalDate endDate;
}
