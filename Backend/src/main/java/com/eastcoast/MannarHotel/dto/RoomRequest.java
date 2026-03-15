package com.eastcoast.MannarHotel.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequest {
    @NotBlank(message = "roomType should not be blank")
    private String roomType;
    @Max(value = 1,message = "room price should be greater then 1")
    private BigDecimal roomPrice;
    private String roomDescription;
}
