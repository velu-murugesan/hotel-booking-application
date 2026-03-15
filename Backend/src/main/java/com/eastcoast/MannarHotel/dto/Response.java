package com.eastcoast.MannarHotel.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private Integer statusCode;
    private String message;
    private String token;
    private String role;
    private String expirationTime;
    private String bookingConfirmationCode;
    private UserResponse user;
    private RoomResponse room;
    private BookingResponse booking;
    private List<UserResponse> userList;
    private List<RoomResponse> roomList;
    private List<BookingResponse> bookingList;

    public Response(String s) {
    }
}
