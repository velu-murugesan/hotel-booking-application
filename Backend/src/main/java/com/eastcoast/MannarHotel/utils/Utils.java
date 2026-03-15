package com.eastcoast.MannarHotel.utils;
import com.eastcoast.MannarHotel.dto.BookingResponse;
import com.eastcoast.MannarHotel.dto.RoomResponse;
import com.eastcoast.MannarHotel.dto.UserResponse;
import com.eastcoast.MannarHotel.entity.Booking;
import com.eastcoast.MannarHotel.entity.Room;
import com.eastcoast.MannarHotel.entity.Users;
import java.security.SecureRandom;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final SecureRandom secureRandom = new SecureRandom();


    public static String generateRandomAlphanumeric(int length){

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {

            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());

            char randomChar =  ALPHANUMERIC_STRING.charAt(randomIndex);

            sb.append(randomChar);

        }

        return sb.toString();

    }

   public static UserResponse convertUserToUserDto(Users user){
       return UserResponse.builder()
               .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .build();
   }

    public static UserResponse convertUserToUserInfo(Users user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .bookingList(user.getBookingList().stream().map(Utils::convertBookingToBookingDto).toList())
                .build();
    }

    public static RoomResponse convertRoomToRoomDto(Room room){
        return RoomResponse.builder()
                .id(room.getId())
                .roomDescription(room.getRoomDescription())
                .roomType(room.getRoomType())
                .roomPrice(room.getRoomPrice())
                .roomPhotoUrl(room.getRoomPhotoUrl())
                .bookings(room.getBookings().stream().map(Utils::convertBookingToBookingDto).collect(Collectors.toList()))
                .build();
    }

    public static BookingResponse convertBookingToBookingDto(Booking booking){
        return BookingResponse.builder()
                .id(booking.getId())
                .bookingConfirmationCode(booking.getBookingConfirmationCode())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .numOfAdults(booking.getNumOfAdults())
                .numOfChildren(booking.getNumOfChildren())
                .totalNumberOfGuest(booking.getTotalNumberOfGuest())
                .room(booking.getRoom())
                .user(booking.getUser())
                .build();
    }

}
