package com.oldlie.zshop.zshopvue.filter;

import com.oldlie.zshop.zshopvue.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private UserService userService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("ZShop ")) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            // parse the token.
            String subject = Jwts.parser()
                    .setSigningKey("z-ship-vue")
                    .parseClaimsJws(token.replace("ZShop ", ""))
                    .getBody()
                    .getSubject();

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
