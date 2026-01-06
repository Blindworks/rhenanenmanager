package com.blindworks.rhenanenmanager.security;

import com.blindworks.rhenanenmanager.domain.entity.User;
import com.blindworks.rhenanenmanager.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.getActivated())
                .accountLocked(user.getAccountLocked())
                .authorities(getAuthorities(user))
                .build();
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (user.getRole() != null) {
            // Add role as authority with ROLE_ prefix
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

            // Add permissions as authorities
            if (user.getRole().getPermissions() != null) {
                authorities.addAll(
                    user.getRole().getPermissions().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
                );
            }
        }

        return authorities;
    }
}
