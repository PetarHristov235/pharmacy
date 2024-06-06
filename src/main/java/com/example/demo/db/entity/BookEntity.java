package com.example.demo.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.domain.Persistable;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookIdSeqGenerator")
    @SequenceGenerator(name = "bookIdSeqGenerator", sequenceName = "book_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "author")
    private String author;

    @Column (name = "genre")
    private String genre;

    @Column(name = "book_details", length = 3000)
    private String bookDetails;

    @Column(name = "stock_count")
    private Integer stockCount;

    @Lob
    @Column(name = "cover")
    private byte[] cover;

    @OneToMany
    @JoinColumn(name = "book_id")
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

        if(rateEntity != null && !rateEntity.isEmpty()) {
            avgRate = BigDecimal.valueOf(rateEntity.stream().mapToInt(RateEntity::getRate).average().orElse(0));
        } else {
            avgRate = BigDecimal.ZERO;
        }
    }
}