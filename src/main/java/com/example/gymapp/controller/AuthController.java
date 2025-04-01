package com.example.gymapp.controller;

import com.example.gymapp.model.Role;
import com.example.gymapp.model.User;
import com.example.gymapp.repository.RoleRepository;
import com.example.gymapp.repository.UserRepository;
import com.example.gymapp.util.JwtUtil;
import com.example.gymapp.dto.LoginRequest;
import com.example.gymapp.dto.AuthResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.ExpiredJwtException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // 🔹 Autorise Angular
public class AuthController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager; // ✅ Ajout de AuthenticationManager

    public AuthController(UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil, 
                          AuthenticationManager authenticationManager) { // ✅ Ajout ici
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 🔹 Enregistrement d'un nouvel utilisateur
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Nom d'utilisateur déjà pris"));
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email déjà enregistré"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
        user.getRoles().add(userRole);

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Utilisateur enregistré avec succès !"));
    }

    /**
     * 🔐 Connexion de l'utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("🔍 Tentative de connexion avec: " + loginRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

            System.out.println("✅ Connexion réussie, token généré: " + jwtToken);

            return ResponseEntity.ok(new AuthResponse(jwtToken));
        } catch (Exception e) {
            System.out.println("❌ Erreur de connexion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Identifiants invalides"));
        }
    }

    /**
     * 🔄 Rafraîchir le token d'authentification
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        try {
            if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Refresh Token manquant"));
            }

            String token = refreshToken.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (jwtUtil.validateToken(token, username)) {
                String newAccessToken = jwtUtil.generateToken(username);
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Refresh Token invalide"));
            }
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Refresh Token expiré. Veuillez vous reconnecter."));
        }
    }
}
