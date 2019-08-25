package com.oldlie.zshop.zshopvue.config.url;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.*;
import java.io.IOException;

public class DynamicallyUrlInterceptor extends AbstractSecurityInterceptor implements Filter {

    //标记自定义的url拦截器已经加载
    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied_dynamically";

    @Setter
    private FilterInvocationSecurityMetadataSource securityMetadataSource;
    private boolean observeOncePerRequest = true;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {

        if ((fi.getRequest() != null)
                && (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
                && observeOncePerRequest) {
            // filter already applied to this request and user wants us to observe
            // once-per-request handling, so don't re-do security checking
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } else {
            // first time this request being called, so perform security checking
            if (fi.getRequest() != null) {
                fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
            }

            InterceptorStatusToken token = super.beforeInvocation(fi);

            try {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            } finally {
                super.finallyInvocation(token);
            }

            super.afterInvocation(token, null);
        }
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        super.setAccessDecisionManager(accessDecisionManager);
    }
}

