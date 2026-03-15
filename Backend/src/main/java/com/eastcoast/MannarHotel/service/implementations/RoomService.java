package com.eastcoast.MannarHotel.service.implementations;
import com.eastcoast.MannarHotel.dto.Response;
import com.eastcoast.MannarHotel.dto.RoomResponse;
import com.eastcoast.MannarHotel.entity.Room;
import com.eastcoast.MannarHotel.exception.OurException;
import com.eastcoast.MannarHotel.repository.BookingRepository;
import com.eastcoast.MannarHotel.repository.RoomRepository;
import com.eastcoast.MannarHotel.service.AwsS3Service;
import com.eastcoast.MannarHotel.service.interfaces.RoomServiceInterface;
import com.eastcoast.MannarHotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService implements RoomServiceInterface {

    private final RoomRepository roomRepository;
   private final BookingRepository bookingRepository;
   private final AwsS3Service service;

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {

        Response response = new Response();

        try{

          String imageUrl = service.saveImageToS3(photo);
          Room room = new Room();
          room.setRoomDescription(description);
          room.setRoomPrice(roomPrice);
          room.setRoomType(roomType);
          room.setRoomPhotoUrl(imageUrl);
         RoomResponse roomDto = Utils.convertRoomToRoomDto(roomRepository.save(room));
         response.setStatusCode(201);
         response.setMessage("room created successfully");
         response.setRoom(roomDto);
        }catch (OurException e){
                response.setStatusCode(400);
                response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room" + e.getMessage());
        }

        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
       return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();
        try{
           List<Room> rooms = roomRepository.findAll();
            response.setStatusCode(200);
            response.setMessage("room created successfully");
            response.setRoomList(rooms.stream().map(Utils::convertRoomToRoomDto).toList());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching a room" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();

        try {
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if (photo != null && !photo.isEmpty()) {
                imageUrl = service.saveImageToS3(photo);
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomDescription(description);
            if (imageUrl != null) room.setRoomPhotoUrl(imageUrl);

            Room updatedRoom = roomRepository.save(room);
            RoomResponse roomDTO = Utils.convertRoomToRoomDto(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            RoomResponse roomDto  = Utils.convertRoomToRoomDto(room);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDto);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();

        try{
           List<Room> rooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate,checkOutDate,roomType);
           response.setStatusCode(200);
           response.setMessage("successful");
           response.setRoomList(rooms.stream().map(Utils::convertRoomToRoomDto).toList());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching a rooms");
        }

        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomList.stream().map(Utils::convertRoomToRoomDto).toList());

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }
}
