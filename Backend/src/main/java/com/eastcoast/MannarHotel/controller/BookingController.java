package com.eastcoast.MannarHotel.controller;
import com.eastcoast.MannarHotel.dto.BookingRequest;
import com.eastcoast.MannarHotel.dto.Response;
import com.eastcoast.MannarHotel.service.interfaces.BookingServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceInterface bookingServiceInterface;

    @PostMapping("/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBooking(@RequestBody BookingRequest bookingRequest,@PathVariable Long roomId,@PathVariable Long userId){
           Response response = bookingServiceInterface.saveBooking(bookingRequest,roomId,userId);
           return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{confirmationCode}")
    public ResponseEntity<Response> findBookingByConfirmationCode(@PathVariable String confirmationCode){
        Response response =  bookingServiceInterface.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long id){
        Response response =  bookingServiceInterface.cancelBooking(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings(){
        Response response = bookingServiceInterface.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
