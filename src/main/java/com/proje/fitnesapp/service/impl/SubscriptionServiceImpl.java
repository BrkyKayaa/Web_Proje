package com.proje.fitnesapp.service.impl;

import com.proje.fitnesapp.model.Membership;
import com.proje.fitnesapp.model.Subscription;
import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.repository.MembershipRepository;
import com.proje.fitnesapp.repository.SubscriptionRepository;
import com.proje.fitnesapp.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Override
    public void createSubscription(User user, Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Üyelik bulunamadı."));

        boolean alreadyHas = user.getSubscriptions().stream()
                .anyMatch(s -> s.getMembership().getId().equals(membershipId));

        if (alreadyHas) {
            throw new IllegalStateException("Bu üyeliği zaten satın aldınız.");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setMembership(membership);

        LocalDate now = LocalDate.now();
        subscription.setStartDate(now);
        subscription.setEndDate(now.plusDays(membership.getDurationInDays()));

        subscriptionRepository.save(subscription);
    }


    @Override
    public List<Subscription> getUserSubscriptions(User user) {
        return subscriptionRepository.findByUser(user);
    }
}

