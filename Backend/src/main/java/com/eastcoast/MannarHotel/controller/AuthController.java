package com.eastcoast.MannarHotel.controller;
import com.eastcoast.MannarHotel.dto.LoginRequest;
import com.eastcoast.MannarHotel.dto.Response;
import com.eastcoast.MannarHotel.dto.UserRequest;
import com.eastcoast.MannarHotel.service.implementations.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody UserRequest userRequest){
          Response response =  userService.register(userRequest);
         return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
        Response response =  userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/auth-check")
    public String check(Authentication authentication) {
        if (authentication == null) return "NULL";
        return authentication.getClass().getName() + " -> " + authentication;
    }

}
