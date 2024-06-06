package com.example.demo.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Entity
@Table(name = "medicine")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MedicineEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medicineIdSeqGenerator")
    @SequenceGenerator(name = "medicineIdSeqGenerator", sequenceName = "medicine_id_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "medicine_name")
    private String medicineName;

    @Column(name = "medicine_details", length = 3000)
    private String medicineDetails;

    @Column(name = "stock_count")
    private Integer stockCount;

    @Column(name = "prescription_required")
    private BigDecimal isPrescriptionRequired;
    @Lob
    @Column(name = "cover")
    private byte[] cover;

    @OneToMany
    @JoinColumn(name = "medicine_id")
    private List<RateEntity> rateEntity;

    @Transient
    private String coverBase64encoded;

    @Transient
    private BigDecimal avgRate;

    @Override
    public boolean isNew() {
        return id == null;
    }

    @PostLoad
    @PreUpdate
    private void encodeCoverBase64() {
        if (cover != null) {
            byte[] encodeBase64 = Base64.encodeBase64(cover);
            String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
            coverBase64encoded = base64Encoded;
        }

        if (rateEntity != null && !rateEntity.isEmpty()) {
            avgRate = BigDecimal.valueOf(rateEntity.stream().mapToInt(RateEntity::getRate).average().orElse(0));
        } else {
            avgRate = BigDecimal.ZERO;
        }
    }
}