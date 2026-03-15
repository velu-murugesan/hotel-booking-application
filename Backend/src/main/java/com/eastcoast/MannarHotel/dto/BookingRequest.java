package com.eastcoast.MannarHotel.dto;
import com.eastcoast.MannarHotel.entity.Room;
import com.eastcoast.MannarHotel.entity.Users;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
    @NotNull(message = "check in date is required")
    private LocalDate checkInDate;
    @NotNull(message = "check out date is required")
    @Future(message = "check out date must be in the future")
    private LocalDate checkOutDate;
    @Min(value = 1, message = "Number of adults must be greater then 0")
    private Integer numOfAdults;
    @Min(value = 0, message = "Number of children must not be negative")
    private Integer numOfChildren;
    private Long userId;
    private Long roomId;
}
