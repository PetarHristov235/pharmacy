package com.example.demo.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "\"rate\"")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RateEntity implements Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rateIdSeqGenerator")
    @SequenceGenerator(name = "rateIdSeqGenerator", sequenceName = "rate_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "rate_stars")
    private Short rate;

    @Column(name = "comment",length = 3000)
    private String comment;

    @Column(name = "medicine_id")
    private Long medicineId;

    @Override
    public boolean isNew() {
        return false;
    }
}
