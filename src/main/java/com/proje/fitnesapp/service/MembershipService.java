package com.proje.fitnesapp.service;

import com.proje.fitnesapp.dto.MembershipDto;
import com.proje.fitnesapp.model.Membership;
import com.proje.fitnesapp.model.MembershipType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MembershipService {
    void create(MembershipDto dto) throws IOException;
    List<Membership> getAll();
    Membership getById(Long id);
    List<Membership> getByType(MembershipType type);
    void updateMembership(Long id, String title, String description, Double price,
                          Integer durationInDays, MembershipType type, MultipartFile imageFile) throws IOException;

    void delete(Long id);

}
