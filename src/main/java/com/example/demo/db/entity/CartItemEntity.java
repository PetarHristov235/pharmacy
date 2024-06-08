package com.example.demo.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartItemIdSeqGenerator")
    @SequenceGenerator(name = "cartItemIdSeqGenerator", sequenceName = "cart_item_id_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "cart_number")
    Long cartNumber;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    MedicineEntity medicine;

    @Column (name = "quantity")
    Integer quantity;
}
