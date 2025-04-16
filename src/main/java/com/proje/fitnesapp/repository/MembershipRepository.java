package com.proje.fitnesapp.repository;

import com.proje.fitnesapp.model.Membership;
import com.proje.fitnesapp.model.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByType(MembershipType type);
}