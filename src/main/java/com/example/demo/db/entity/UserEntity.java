package com.example.demo.db.entity;

import com.example.demo.db.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdSeqGenerator")
    @SequenceGenerator(name = "userIdSeqGenerator", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false, length = 32)
    private String username;
    @Column(nullable = false, unique = true, length = 128)
    private String email;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 128)
    private String password;
    private boolean active = true;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role roles;

    @Override
    public boolean isNew() {
        return id == null;
    }
}