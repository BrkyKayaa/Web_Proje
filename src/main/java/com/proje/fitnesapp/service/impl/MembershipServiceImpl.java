package com.proje.fitnesapp.service.impl;

import com.proje.fitnesapp.dto.MembershipDto;
import com.proje.fitnesapp.model.Membership;
import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.repository.MembershipRepository;
import com.proje.fitnesapp.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MembershipServiceImpl implements MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Override
    public void create(MembershipDto dto) throws IOException {
        Membership membership = new Membership();
        membership.setTitle(dto.getTitle());
        membership.setDescription(dto.getDescription());
        membership.setPrice(dto.getPrice());
        membership.setDurationInDays(dto.getDurationInDays());
        membership.setType(dto.getType());

        try{
            if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
                byte[] bytes = dto.getImageFile().getBytes();
                Byte[] byteObjects = new Byte[bytes.length];
                for (int i = 0; i < bytes.length; i++) byteObjects[i] = bytes[i];
                membership.setImage(byteObjects);
            }
        }catch (IOException e){
            throw new RuntimeException("Görsel yükleme hatası" + e.getMessage());
        }
        membershipRepository.save(membership);
    }

    @Override
    public List<Membership> getAll() {
        return membershipRepository.findAll();
    }
    @Override
    public List<Membership> getByType(MembershipType type) {
        return membershipRepository.findByType(type);
    }

    @Override
    public Membership getById(Long id) {
        return membershipRepository.findById(id).orElse(null);
    }
    @Override
    public void updateMembership(Long id, String title, String description, Double price,
                                 Integer durationInDays, MembershipType type, MultipartFile imageFile) throws IOException {

        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Üyelik bulunamadı: " + id));

        membership.setTitle(title);
        membership.setDescription(description);
        membership.setPrice(price);
        membership.setDurationInDays(durationInDays);
        membership.setType(type);

        if (imageFile != null && !imageFile.isEmpty()) {
            Byte[] imageBytes = new Byte[imageFile.getBytes().length];
            int i = 0;
            for (byte b : imageFile.getBytes()) {
                imageBytes[i++] = b;
            }
            membership.setImage(imageBytes);
        }

        membershipRepository.save(membership);
    }

    @Override
    public void delete(Long id) {
        membershipRepository.deleteById(id);
    }
}
