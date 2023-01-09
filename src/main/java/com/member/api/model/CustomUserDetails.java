package com.member.api.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final MemberEntity memberEntity;
    public CustomUserDetails(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }
    public final MemberEntity getMember() {
        return memberEntity;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return memberEntity.getRoles().stream().map(o -> new SimpleGrantedAuthority(o.getName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return memberEntity.getPassword();
    }
    @Override
    public String getUsername() {
        return memberEntity.getUserId();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}