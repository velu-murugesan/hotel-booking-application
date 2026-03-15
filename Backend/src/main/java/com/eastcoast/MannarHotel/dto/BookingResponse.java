package com.eastcoast.MannarHotel.dto;
import com.eastcoast.MannarHotel.entity.Room;
import com.eastcoast.MannarHotel.entity.Users;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponse {
    private Long id;
private LocalDate checkInDate;
private LocalDate checkOutDate;
private Integer numOfAdults;
private Integer numOfChildren;
private Integer totalNumberOfGuest;
private String bookingConfirmationCode;
private Users user;
private Room room;
}


