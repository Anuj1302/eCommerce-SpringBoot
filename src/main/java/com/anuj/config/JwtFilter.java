package com.anuj.config;

import com.anuj.service.JWTService;
import com.anuj.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;
//        String path = request.getServletPath();
//
//        // 🔥 SKIP JWT for auth endpoints
//        if (path.startsWith("/auth/")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        System.out.println("JWT FILTER → " + request.getRequestURI());
        String path = request.getServletPath();

        if (
                path.equals("/auth/login") ||
                        path.equals("/auth/register") ||
                        path.equals("/auth/refresh") ||

                        path.equals("/getProducts") ||
                        path.equals("/categories") ||
                        path.startsWith("/getProducts/") ||
                        path.startsWith("/products/search") ||
                        (path.startsWith("/product/") && path.endsWith("/image"))
        ) {
            filterChain.doFilter(request, response);
            return;
        }



        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
        }
        try {
            if (token != null) {
                userName = jwtService.extractUserName(token);
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // ✅ Access token expired → frontend will refresh
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception e) {
            // Invalid token → ignore and continue
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails =context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);
            System.out.println("User: " + userName);
            System.out.println("Authorities: " + userDetails.getAuthorities());
            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}
