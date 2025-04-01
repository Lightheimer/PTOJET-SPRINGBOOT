package com.example.gymapp.controller;

import com.example.gymapp.model.User;
import com.example.gymapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔹 Récupérer tous les utilisateurs (Admin uniquement)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 🔹 Récupérer un utilisateur par ID (Admin uniquement)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔥 Supprimer un utilisateur (Admin uniquement)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("Utilisateur supprimé avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 Modifier son propre profil (Utilisateur connecté)
    @PutMapping("/update-profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> updates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Utilisateur non trouvé"));
        }

        User user = userOptional.get();
        if (updates.containsKey("email")) {
            user.setEmail(updates.get("email"));
        }
        if (updates.containsKey("username")) {
            user.setUsername(updates.get("username"));
        }
        userService.saveUser(user);

        return ResponseEntity.ok(Map.of("message", "Profil mis à jour avec succès !"));
    }

    // 🔹 Changer son mot de passe (Utilisateur connecté)
    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordData) {
        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); 

        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Utilisateur non trouvé"));
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Ancien mot de passe incorrect"));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);

        return ResponseEntity.ok(Map.of("message", "Mot de passe mis à jour avec succès !"));
    }

    // 🔹 Réinitialisation du mot de passe via email
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        String newPassword = requestData.get("newPassword");
        String confirmPassword = requestData.get("confirmPassword");

        try {
            userService.updatePasswordByEmail(email, newPassword, confirmPassword);
            return ResponseEntity.ok(Map.of("message", "Mot de passe mis à jour avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
