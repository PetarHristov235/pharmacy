package com.example.demo.security.confg;

import com.example.demo.db.entity.UserEntity;
import com.example.demo.db.entity.enums.Role;
import com.example.demo.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);


        if (user != null) {
            if(!user.isActive()){
                try {
                    throw new Exception("The account is inactive!");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    mapRolesToAuthorities(List.of(user.getRoles())));

        } else {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).toList();
    }
}
