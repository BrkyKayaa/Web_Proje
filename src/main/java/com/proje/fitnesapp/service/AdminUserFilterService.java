package com.proje.fitnesapp.service;

import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.model.User;

import java.util.List;

public interface AdminUserFilterService {
    List<User> getAllUsers();
    List<User> getUsersByMembershipType(MembershipType type);
}