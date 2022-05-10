package ru.skqwk.elearningsystem.model.enumeration;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

public enum UserRole {
    STUDENT,
    TEACHER,
    ADMIN;


    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }
}
