package com.example.websocketdemo.repositories;

import com.example.websocketdemo.enums.RoomType;
import com.example.websocketdemo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query(value = "SELECT DISTINCT r.ID FROM ROOM as r LEFT JOIN ROOM_USERS as lu on r.ID=lu.ROOM_ID LEFT JOIN ROOM_USERS as ru on r.ID=ru.ROOM_ID \n" +
            "WHERE r.ROOM_TYPE=1 AND lu.USERS_ID = ?1 AND ru.USERS_ID = ?2", nativeQuery = true)
    Integer findByRoomTypeAndUsers(int user1, int user2);
    Optional<Room> findByIdAndRoomType(int id, RoomType roomType);
    List<Room> findAllByRoomType(RoomType type);
}
