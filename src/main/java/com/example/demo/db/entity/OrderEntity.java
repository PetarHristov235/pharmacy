package com.example.demo.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;

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

    @Column(name = "title")
    private String title;

    @Column (name = "date")
    private LocalDate date;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    public OrderEntity(String address, LocalDate date, String phoneNumber, String title, String username) {
        this.address = address;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.title = title;
        this.username = username;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
