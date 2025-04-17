package com.proje.fitnesapp.service.impl;

import com.proje.fitnesapp.model.Membership;
import com.proje.fitnesapp.model.Subscription;
import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.repository.MembershipRepository;
import com.proje.fitnesapp.repository.SubscriptionRepository;
import com.proje.fitnesapp.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Kullanıcıların üyelik abonelik işlemlerini yöneten servis katmanıdır.
 */
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final MembershipRepository membershipRepository;

    /**
     * Bir kullanıcı için yeni üyelik aboneliği oluşturur.
     *
     * @param user         aboneliği oluşturacak kullanıcı
     * @param membershipId abonelik yapılacak üyelik paketi ID'si
     * @throws IllegalStateException kullanıcı zaten bu üyeliğe sahipse
     * @throws IllegalArgumentException belirtilen üyelik bulunamazsa
     */
    @Override
    public void createSubscription(User user, Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new IllegalArgumentException("Üyelik bulunamadı: " + membershipId));

        if (hasExistingSubscription(user, membershipId)) {
            throw new IllegalStateException("Bu üyeliği zaten satın aldınız.");
        }

        Subscription subscription = Subscription.builder()
                .user(user)
                .membership(membership)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(membership.getDurationInDays()))
                .build();

        subscriptionRepository.save(subscription);
    }

    /**
     * Kullanıcının geçmiş veya mevcut tüm aboneliklerini getirir.
     *
     * @param user kullanıcı
     * @return kullanıcının abonelik listesi
     */
    @Override
    public List<Subscription> getUserSubscriptions(User user) {
        return subscriptionRepository.findByUser(user);
    }

    /**
     * Kullanıcının belirtilen üyeliğe zaten sahip olup olmadığını kontrol eder.
     *
     * @param user         kullanıcı
     * @param membershipId kontrol edilecek üyelik ID'si
     * @return true → zaten sahip, false → değil
     */
    private boolean hasExistingSubscription(User user, Long membershipId) {
        return user.getSubscriptions().stream()
                .anyMatch(s -> s.getMembership().getId().equals(membershipId));
    }
}
