package com.eastcoast.MannarHotel.controller;
import com.eastcoast.MannarHotel.dto.Response;
import com.eastcoast.MannarHotel.service.interfaces.BookingServiceInterface;
import com.eastcoast.MannarHotel.service.interfaces.RoomServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomServiceInterface roomServiceInterface;
    private final BookingServiceInterface bookingServiceInterface;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addRoom(  @RequestParam(value = "photo", required = false) MultipartFile photo,
                                              @RequestParam(value = "roomType", required = false) String roomType,
                                              @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
                                              @RequestParam(value = "roomDescription", required = false) String roomDescription){


        if (photo == null || photo.isEmpty() || roomType == null  || roomPrice == null || roomType.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields(photo, roomType,roomPrice)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        Response response = roomServiceInterface.addNewRoom(photo,roomType,roomPrice,roomDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/room-types")
    public ResponseEntity<List<String>> getAllRoomTypes(){
       List<String> rooms =  roomServiceInterface.getAllRoomTypes();
       return ResponseEntity.status(200).body(rooms);
    }

    @GetMapping()
    public ResponseEntity<Response> getAllRooms(){
        Response response =  roomServiceInterface.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<Response> getAllAvailableRooms(
         @PathVariable(required = false) LocalDate checkInDate,  @PathVariable(required = false) LocalDate checkOutDate,  @PathVariable(required = false) String roomType
    ){
        Response response = null;
        if(checkInDate != null && checkOutDate != null && roomType != null){
            response = roomServiceInterface.getAvailableRoomsByDataAndType(checkInDate,checkOutDate,roomType);
        }

       response =  roomServiceInterface.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getRoomById(@PathVariable Long id){
        Response response =  roomServiceInterface.getRoomById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response>  updateRoom(@PathVariable Long id,  @RequestParam(value = "photo", required = false) MultipartFile photo,
                                                @RequestParam(value = "roomType", required = false) String roomType,
                                                @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
                                                @RequestParam(value = "roomDescription", required = false) String roomDescription){
        Response response =  roomServiceInterface.updateRoom(id,roomDescription,roomType,roomPrice,photo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long id){
       Response response = roomServiceInterface.deleteRoom(id);
       return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
