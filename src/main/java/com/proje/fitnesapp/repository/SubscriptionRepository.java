package com.proje.fitnesapp.repository;

import com.proje.fitnesapp.model.Subscription;
import com.proje.fitnesapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser(User user);
}

