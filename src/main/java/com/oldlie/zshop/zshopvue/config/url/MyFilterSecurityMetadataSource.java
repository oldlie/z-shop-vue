package com.oldlie.zshop.zshopvue.config.url;

import com.oldlie.zshop.zshopvue.service.UserService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MyFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;
    private UserService userService;

    public MyFilterSecurityMetadataSource(UserService userService) {
        this.userService = userService;
        Map<RequestMatcher, Collection<ConfigAttribute>> map = new HashMap<>();

        Map<String, String> _map = userService.loadUrlMap();
        for (String key : _map.keySet()) {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(key);
            String[] roles = _map.get(key).split(",");
            ArrayList<ConfigAttribute> configs = new ArrayList<>();
            for (String role : roles) {
                configs.add(new SecurityConfig(role));
            }
            map.put(matcher, configs);
        }

        this.requestMap = map;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        HttpServletRequest request = fi.getRequest();

        // Lookup your database (or other source) using this information and populate the
        // list of attributes (这里初始话你的权限数据)

        //遍历我们初始化的权限数据，找到对应的url对应的权限
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : this.requestMap
                .entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
