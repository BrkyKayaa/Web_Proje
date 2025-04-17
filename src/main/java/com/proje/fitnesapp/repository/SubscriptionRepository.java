package com.proje.fitnesapp.repository;

import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.model.Subscription;
import com.proje.fitnesapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser(User user);

    @Query("SELECT DISTINCT s.user FROM Subscription s WHERE s.membership.type = :type")
    List<User> findDistinctUsersByMembershipType(@Param("type") MembershipType type);

}

