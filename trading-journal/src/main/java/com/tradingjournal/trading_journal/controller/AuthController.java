package com.tradingjournal.trading_journal.controller;

import com.tradingjournal.trading_journal.dtos.*;
import com.tradingjournal.trading_journal.models.User;
import com.tradingjournal.trading_journal.service.UserService;
import com.tradingjournal.trading_journal.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest signup) {
        if (userService.emailExists(signup.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use.");
        }
        if (userService.usernameExists(signup.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken.");
        }

        User saved = userService.registerUser(signup);
        UserDTO dto = new UserDTO(saved.getId(), saved.getUsername(), saved.getEmail());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication.getName()); // email stored as principal
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email);
        if (user == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new UserDTO(user.getId(), user.getUsername(), user.getEmail()));
    }
}
