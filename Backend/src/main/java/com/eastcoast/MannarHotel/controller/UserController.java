package com.eastcoast.MannarHotel.controller;
import com.eastcoast.MannarHotel.dto.Response;
import com.eastcoast.MannarHotel.service.implementations.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(){
         Response response = userService.getAllUsers();
         return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserByUserId(@PathVariable Long id){
        Response response = userService.getUserById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id){
        Response response = userService.deleteUser(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user-info")
    public ResponseEntity<Response> getUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Response response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user-history/{id}")
    public ResponseEntity<Response> getUserBookingsHistory(@PathVariable Long id){
        Response response = userService.getUserBookingHistory(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
