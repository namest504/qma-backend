package com.l1mit.qma_server.global.auth.oauth.dto;

import com.l1mit.qma_server.domain.member.domain.Member;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class PrincipalUser implements UserDetails, OidcUser, OAuth2User {

    private final Member member;
    private final Map<String, Object> attribute;
    private final Collection<? extends GrantedAuthority> authorities;

    @Builder
    public PrincipalUser(final Member member) {

        Map<String, Object> attributes = new HashMap<>() {{
            put("id", member.getId());
        }};

        this.member = member;
        this.attribute = attributes;
        this.authorities = member.getRole();
    }

    public Long getId() {
        return member.getId();
    }

    @Override
    public String getName() {
        return member.getId().toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attribute;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public String getUsername() {
        return member.getId().toString();
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

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}
