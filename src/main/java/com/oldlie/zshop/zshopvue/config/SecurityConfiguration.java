package com.oldlie.zshop.zshopvue.config;

import com.oldlie.zshop.zshopvue.config.url.DynamicallyUrlAccessDecisionManager;
import com.oldlie.zshop.zshopvue.config.url.DynamicallyUrlInterceptor;
import com.oldlie.zshop.zshopvue.config.url.MyFilterSecurityMetadataSource;
import com.oldlie.zshop.zshopvue.config.url.RoleBasedVoter;
import com.oldlie.zshop.zshopvue.filter.JWTAuthenticationFilter;
import com.oldlie.zshop.zshopvue.filter.JWTLoginFilter;
import com.oldlie.zshop.zshopvue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserService userService;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                //.exceptionHandling()
                //.authenticationEntryPoint(problemSupport)
                //.accessDeniedHandler(problemSupport)
                //.and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .addFilterAfter(dynamicallyUrlInterceptor(), FilterSecurityInterceptor.class)
                .authorizeRequests()
                .antMatchers("/", "/name", "/quite").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTLoginFilter(authenticationManager()))
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), this.userService));
        http.logout().logoutSuccessUrl("/quite");
    }



    @Bean
    public DynamicallyUrlInterceptor dynamicallyUrlInterceptor(){
        DynamicallyUrlInterceptor interceptor = new DynamicallyUrlInterceptor();
        interceptor.setSecurityMetadataSource(new MyFilterSecurityMetadataSource(this.userService));

        //配置RoleVoter决策
        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new RoleBasedVoter(this.userService));
        //设置认证决策管理器
        interceptor.setAccessDecisionManager(new DynamicallyUrlAccessDecisionManager(decisionVoters));
        return interceptor;
    }
}
