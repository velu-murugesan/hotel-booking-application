package com.eastcoast.MannarHotel.service.interfaces;

import com.eastcoast.MannarHotel.dto.BookingRequest;
import com.eastcoast.MannarHotel.dto.Response;
import com.eastcoast.MannarHotel.entity.Booking;

public interface BookingServiceInterface {


    Response saveBooking(BookingRequest bookingRequest,Long roomId,Long userId);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);

}
