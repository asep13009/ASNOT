package id.app.asnot.controller;

import id.app.asnot.model.entity.User;
import id.app.asnot.model.request.LoginRequest;
import id.app.asnot.model.request.LoginResponse;
import id.app.asnot.model.response.UserRequest;
import id.app.asnot.service.AuthService;
import id.app.asnot.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }
    @CrossOrigin("*")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request)    {
        System.out.println("login>> "+request);
        User user = authService.authenticate(request.getUsername(), request.getPassword());
        if (user != null) {
            System.out.println("generate token");
            String token = jwtService.generateToken(user.getUsername(), user.getRole().name(), user.getId());
            UserRequest userRequest = new UserRequest();
            userRequest.setUsername(user.getUsername());
            userRequest.setName(user.getName());
            userRequest.setEmail(user.getEmail());
            userRequest.setRole(user.getRole().name());
            return ResponseEntity.ok(new LoginResponse(token, user.getRole().name(), userRequest));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
    @CrossOrigin("*")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody User user) {
        try {
            System.out.println("register"+user);
            User savedUser = authService.registerUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", savedUser.getId());
            userData.put("username", savedUser.getUsername());
            userData.put("email", savedUser.getEmail());
            response.put("user", userData);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Internal server error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}