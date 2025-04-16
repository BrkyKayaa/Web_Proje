package com.proje.fitnesapp.service.impl;

import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.model.Subscription;
import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.repository.SubscriptionRepository;
import com.proje.fitnesapp.repository.UserRepository;
import com.proje.fitnesapp.service.AdminUserFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminUserFilterServiceImpl implements AdminUserFilterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByMembershipType(MembershipType type) {
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        // Aynı kullanıcının birden fazla üyeliği varsa tekrar etmesin
        return subscriptions.stream()
                .filter(sub -> sub.getMembership().getType() == type)
                .map(Subscription::getUser)
                .distinct()
                .collect(Collectors.toList());
    }
}