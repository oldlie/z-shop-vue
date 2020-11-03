package com.oldlie.zshop.zshopvue.filter;

import com.google.gson.Gson;
import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.service.UserService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private UserService userService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 这里相当于打了一个补丁排除了允许匿名房访问的URL，处理方式比较粗暴恶心，后面得想办法解决掉
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/public/**");
        if (matcher.matches(request)) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("ZShop ")) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = null;
        try {
            authentication = getAuthentication(request);
        } catch (Exception e) {
            e.printStackTrace();
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setStatus(HTTP_CODE.EXPIRED);
            baseResponse.setMessage("登录凭证过期，请重新登录");
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(baseResponse));
            response.getWriter().flush();
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws Exception{
        log.info("request uri: {}", request.getRequestURI());
        String token = request.getHeader("Authorization");
        if (token != null) {
            // parse the token.
            String subject = null;

            try {
                subject = Jwts.parser()
                        .setSigningKey("z-ship-vue")
                        .parseClaimsJws(token.replace("ZShop ", ""))
                        .getBody()
                        .getSubject();
            } catch (Exception e) {
                return null;
            }

            if (subject != null) {
                String[] tmp = subject.split("@");
                if (tmp.length != 2) {
                    throw new RuntimeException("Invalid authentication");
                }
                UserDetails userDetails = this.userService.loadUserByUsername(tmp[1]);

                HttpSession session = request.getSession();
                session.setAttribute("uid", tmp[0]);
                session.setAttribute("username", tmp[1]);

                return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                        null,
                        userDetails.getAuthorities());
            }
            return null;
        }
        return null;
    }
}
