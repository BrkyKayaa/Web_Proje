package com.proje.fitnesapp.service;

import com.proje.fitnesapp.model.Subscription;
import com.proje.fitnesapp.model.User;

import java.util.List;

public interface SubscriptionService {
    void createSubscription(User user, Long membershipId);
    List<Subscription> getUserSubscriptions(User user);
}