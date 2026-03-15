package com.eastcoast.MannarHotel.repository;

import com.eastcoast.MannarHotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByRoomId(Long roomId);

    Optional<Booking> findBookingByBookingConfirmationCode(String confirmationCode);

    List<Booking> findBookingsByUserId(Long id);
}
