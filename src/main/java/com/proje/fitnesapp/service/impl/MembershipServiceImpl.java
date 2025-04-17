package com.proje.fitnesapp.service.impl;

import com.proje.fitnesapp.dto.MembershipDto;
import com.proje.fitnesapp.model.Membership;
import com.proje.fitnesapp.model.MembershipType;
import com.proje.fitnesapp.repository.MembershipRepository;
import com.proje.fitnesapp.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Üyelik paketleri ile ilgili tüm CRUD işlemlerini gerçekleştiren servis katmanıdır.
 */
@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;

    /**
     * Yeni bir üyelik paketi oluşturur.
     *
     * @param dto oluşturulacak üyelik bilgileri
     * @throws IOException görsel dosyası okunurken hata oluşursa
     */
    @Override
    public void create(MembershipDto dto) throws IOException {
        Membership membership = Membership.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .durationInDays(dto.getDurationInDays())
                .type(dto.getType())
                .image(convertToBytes(dto.getImageFile()))
                .build();

        membershipRepository.save(membership);
    }

    /**
     * Tüm üyelikleri döner.
     *
     * @return üyelik listesi
     */
    @Override
    public List<Membership> getAll() {
        return membershipRepository.findAll();
    }

    /**
     * Belirtilen türe sahip üyelikleri döner.
     *
     * @param type üyelik türü
     * @return filtrelenmiş üyelik listesi
     */
    @Override
    public List<Membership> getByType(MembershipType type) {
        return membershipRepository.findByType(type);
    }

    /**
     * ID'ye göre üyeliği getirir.
     *
     * @param id üyelik ID'si
     * @return bulunan üyelik veya null
     */
    @Override
    public Membership getById(Long id) {
        return membershipRepository.findById(id).orElse(null);
    }

    /**
     * Belirli bir üyeliği günceller.
     *
     * @param id üyelik ID'si
     * @param title başlık
     * @param description açıklama
     * @param price fiyat
     * @param durationInDays süre
     * @param type üyelik türü
     * @param imageFile yeni görsel (isteğe bağlı)
     * @throws IOException görsel yüklenirken hata oluşursa
     */
    @Override
    public void updateMembership(Long id, String title, String description, Double price,
                                 Integer durationInDays, MembershipType type, MultipartFile imageFile) throws IOException {

        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Üyelik bulunamadı: " + id));

        membership.setTitle(title);
        membership.setDescription(description);
        membership.setPrice(price);
        membership.setDurationInDays(durationInDays);
        membership.setType(type);

        if (imageFile != null && !imageFile.isEmpty()) {
            membership.setImage(convertToBytes(imageFile));
        }

        membershipRepository.save(membership);
    }

    /**
     * Belirli bir üyeliği siler.
     *
     * @param id üyelik ID'si
     */
    @Override
    public void delete(Long id) {
        membershipRepository.deleteById(id);
    }

    /**
     * MultipartFile dosyasını byte[] dizisine dönüştürür.
     *
     * @param file görsel dosyası
     * @return byte dizisi veya null
     * @throws IOException dosya okunamazsa
     */
    private byte[] convertToBytes(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            return file.getBytes();
        }
        return null;
    }
}
