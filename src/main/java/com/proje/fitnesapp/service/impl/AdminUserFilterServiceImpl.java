package com.proje.fitnesapp.service.impl;

import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.model.User;
import com.proje.fitnesapp.repository.SubscriptionRepository;
import com.proje.fitnesapp.repository.UserRepository;
import com.proje.fitnesapp.service.AdminUserFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Admin paneli için kullanıcı filtreleme işlemlerini gerçekleştiren servis sınıfıdır.
 * Üyelik türüne göre kullanıcıları listeleme gibi işlemleri içerir.
 */
@Service
@RequiredArgsConstructor
public class AdminUserFilterServiceImpl implements AdminUserFilterService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    /**
     * Sistemde kayıtlı olan tüm kullanıcıları getirir.
     *
     * @return Tüm kullanıcıların listesi.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Belirli bir üyelik türüne sahip olan kullanıcıları getirir.
     * Aynı kullanıcı birden fazla üyeliğe sahipse sadece bir kez döner.
     * İşlem veritabanı seviyesinde gerçekleştirilir.
     *
     * @param type Filtrelenecek üyelik türü.
     * @return Bu üyelik türüne sahip kullanıcıların listesi.
     */
    @Override
    public List<User> getUsersByMembershipType(MembershipType type) {
        return subscriptionRepository.findDistinctUsersByMembershipType(type);
    }
}
