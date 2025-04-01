package com.example.gymapp.service;

import com.example.gymapp.model.User;
import com.example.gymapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 🔹 Mettre à jour le mot de passe via l'email (sans `resetToken`)
    public void updatePasswordByEmail(String email, String newPassword, String confirmPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Aucun utilisateur trouvé avec cet email");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword)); // 🔐 Hachage du mot de passe
        userRepository.save(user);
    }
}
