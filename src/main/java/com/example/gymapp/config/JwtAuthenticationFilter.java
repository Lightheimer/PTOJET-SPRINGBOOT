package com.example.gymapp.config;

import com.example.gymapp.service.UserDetailsServiceImpl;
import com.example.gymapp.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
    
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("⚠️ Aucun token trouvé dans le header.");
                filterChain.doFilter(request, response);
                return;
            }
    
            jwt = authHeader.substring(7);
            System.out.println("🔍 Token extrait : " + jwt);
            username = jwtUtil.extractUsername(jwt);
            System.out.println("👤 Username extrait du token : " + username);
    
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("🔐 Validation du token...");
    
                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    System.out.println("✅ Token valide !");
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("❌ Token invalide !");
                }
            }
    
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Votre session a expiré. Veuillez vous reconnecter.\"}");
        } catch (Exception e) {
            System.out.println("❌ Problème avec le token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Token invalide.\"}");
        }
    }
    }
