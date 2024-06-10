package com.example.demo.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "\"order\"")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderEntity implements Persistable<Long> {

    @Id
    @SequenceGenerator(name="orderSequence", initialValue=100000,allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="orderSequence")
    private Long id;

    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<CartItemEntity> cartItems;

    @Column (name = "items")
    private String items;

    @Column (name = "date")
    private LocalDate date;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public OrderEntity(String address, LocalDate date, String phoneNumber, String items, String username, BigDecimal totalPrice) {
        this.address = address;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.items = items;
        this.username = username;
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
