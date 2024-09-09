package org.example.securingapi.controller;

import org.example.securingapi.model.AuthRequest;
import org.example.securingapi.model.AuthResponse;
import org.example.securingapi.security.JwtTokenUtil;
import org.example.securingapi.service.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new user.
     * @param authRequest the authentication request containing username and password.
     * @return ResponseEntity with the registration result.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest authRequest) {
        if (userDetailsService.userExists(authRequest.getUsername())) {
            return ResponseEntity.badRequest().body("User already exists!");
        }

        // Encode the password only once
        String rawPassword = authRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Log the raw and encoded passwords
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);

        // Add user without encoding again inside addUser()
        userDetailsService.addUser(authRequest.getUsername(), encodedPassword, "USER");

        return ResponseEntity.ok(new AuthResponse("User registered successfully"));
    }



    /**
     * Login user and generate JWT token.
     * @param authRequest the authentication request containing username and password.
     * @return ResponseEntity with the JWT token or an error message.
     * @throws Exception in case of any issue during login.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            // Authenticate the user with the provided username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for user '{}'", authRequest.getUsername());
            return ResponseEntity.status(401).body("Incorrect username or password");
        }

        // Load the user details if authentication is successful
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // Generate a JWT token using the user details
        String jwtToken = jwtTokenUtil.generateToken(userDetails);

        // Log the generated JWT token (for debugging purposes, be careful with logging sensitive data in production)
        logger.info("Generated JWT Token for user '{}': {}", authRequest.getUsername(), jwtToken);

        // Return the JWT token in the response
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    /**
     * Validate the provided JWT token.
     * @param token The JWT token to validate.
     * @return ResponseEntity with validation result.
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
        String username = jwtTokenUtil.extractUsername(token);

        // Load user details to validate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.validateToken(token, userDetails)) {
            logger.info("Token for user '{}' is valid", username);
            return ResponseEntity.ok("Token is valid");
        } else {
            logger.warn("Token for user '{}' is invalid", username);
            return ResponseEntity.status(401).body("Token is invalid");
        }
    }
}
