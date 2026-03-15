package com.eastcoast.MannarHotel.service.implementations;
import com.eastcoast.MannarHotel.dto.BookingResponse;
import com.eastcoast.MannarHotel.dto.BookingRequest;
import com.eastcoast.MannarHotel.dto.Response;
import com.eastcoast.MannarHotel.entity.Booking;
import com.eastcoast.MannarHotel.entity.Room;
import com.eastcoast.MannarHotel.entity.Users;
import com.eastcoast.MannarHotel.exception.OurException;
import com.eastcoast.MannarHotel.repository.BookingRepository;
import com.eastcoast.MannarHotel.repository.RoomRepository;
import com.eastcoast.MannarHotel.repository.UserRepository;
import com.eastcoast.MannarHotel.service.interfaces.BookingServiceInterface;
import com.eastcoast.MannarHotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements BookingServiceInterface {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public Response saveBooking(BookingRequest bookingRequest,Long roomId,Long userId) {

        Response response = new Response();

        try{

            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new IllegalArgumentException("Check in date must come before check out date");
            }

            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            Users user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<Booking> existingBookings = room.getBookings();

            if(!roomIsAvailable(bookingRequest,existingBookings)){
                throw new OurException("Room is not available for selected date range");
            }

            Booking booking = new Booking();
            booking.setRoom(room);
            booking.setUser(user);
            booking.setNumOfAdults(bookingRequest.getNumOfAdults());
            booking.setNumOfChildren(bookingRequest.getNumOfChildren());
            booking.setCheckInDate(bookingRequest.getCheckInDate());
            booking.setCheckOutDate(bookingRequest.getCheckOutDate());
            String bookingConfirmationCode = Utils.generateRandomAlphanumeric(10);
            booking.setBookingConfirmationCode(bookingConfirmationCode);
             bookingRepository.save(booking);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(500);
            response.setMessage("");
        }

        return response;

    }

//    a booking 4 to 6 and another one 5 to 7;

    private boolean roomIsAvailable(BookingRequest bookingRequest, List<Booking> existingBookings) {

        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
                        &&  bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckInDate())

                );
    }
    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findBookingByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new OurException("Booking Not Found"));
            BookingResponse bookingDTO = Utils.convertBookingToBookingDto(booking);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBooking(bookingDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Finding a booking: " + e.getMessage());

        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingResponse> bookingDTOList = bookingList.stream().map(Utils::convertBookingToBookingDto).toList();
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingList(bookingDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Getting all bookings: " + e.getMessage());

        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try {
            bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking Does Not Exist"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Cancelling a booking: " + e.getMessage());

        }
        return response;
    }
}
