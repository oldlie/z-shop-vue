package com.oldlie.zshop.zshopvue.config.url;

import com.oldlie.zshop.zshopvue.service.UserService;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RoleBasedVoter implements AccessDecisionVoter<Object> {

    private UserService userService;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public RoleBasedVoter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object o, Collection<ConfigAttribute> attributes) {
        if (authentication == null) {
            return ACCESS_DENIED;
        }

        FilterInvocation fi = (FilterInvocation) o;
        String url = fi.getRequestUrl();

        int result = ACCESS_ABSTAIN;
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication, url);

        for (ConfigAttribute attribute : attributes) {
            if (attribute.getAttribute() == null) {
                continue;
            }
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;

                // Attempt to find a matching granted authority
                for (GrantedAuthority authority : authorities) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }

        return result;
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(
            Authentication authentication, String url) {
        return authentication.getAuthorities();
    }
}

