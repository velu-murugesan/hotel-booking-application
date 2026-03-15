package com.eastcoast.MannarHotel.repository;
import com.eastcoast.MannarHotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("select distinct r.roomType from Room r")
     List<String> findDistinctRoomTypes();

    @Query("select r from Room r where r.id not in (select b.room.id from Booking b) ")
    List<Room> getAllAvailableRooms();

    @Query(" select r from Room r where lower(r.roomType) like lower(concat('%',:roomType,'%')) and r.id not in ( select b.room.id from Booking b where (b.checkInDate <= :checkOutDate) AND (b.checkOutDate >= :checkInDate))")
    List<Room> findAvailableRoomsByDatesAndTypes(
            @Param("checkInDate") LocalDate checkInDate, @Param("checkOutDate") LocalDate checkOutDate,@Param("roomType") String roomType);



}
