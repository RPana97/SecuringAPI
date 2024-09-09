package org.example.securingapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JwtUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        initUsers();  // Initialize users after PasswordEncoder is injected
    }

    private void initUsers() {
        UserDetails user = User.withUsername("testuser")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        users.put("testuser", user);

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("adminpass"))
                .roles("ADMIN")
                .build();
        users.put("admin", admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (users.containsKey(username)) {
            UserDetails user = users.get(username);
            // Log to check the retrieved user and encoded password
            System.out.println("Loaded user: " + username + " with password: " + user.getPassword());
            return user;
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public void addUser(String username, String encodedPassword, String role) {
        // Use the already encoded password, do not encode it again
        UserDetails newUser = User.withUsername(username)
                .password(encodedPassword)  // Use the encoded password
                .roles(role)
                .build();
        users.put(username, newUser);
    }


    public boolean userExists(String username) {
        return users.containsKey(username);
    }
}
