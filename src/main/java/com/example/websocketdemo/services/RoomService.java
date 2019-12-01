package com.example.websocketdemo.services;

import com.example.websocketdemo.dtos.RoomDTO;
import com.example.websocketdemo.enums.RoomType;
import com.example.websocketdemo.model.Room;
import com.example.websocketdemo.model.User;
import com.example.websocketdemo.repositories.RoomRepository;
import com.example.websocketdemo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository, ModelMapper mapper) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAllByRoomType(RoomType.PUBLIC);
    }

    public RoomDTO synchronizeUserWithRooms(int newRoomId, Integer oldRoomId, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user with given id cannot be found"));
        Room newRoom = roomRepository.findByIdAndRoomType(newRoomId, RoomType.PUBLIC)
                .orElseThrow(() -> new EntityNotFoundException("room with given id cannot be found"));

        if (oldRoomId == null) {
            newRoom.getUsers().add(user);
        } else {
            Room oldRoom = roomRepository.findById(oldRoomId)
                    .orElseThrow(() -> new EntityNotFoundException("room with given id cannot be found"));
            oldRoom.getUsers().remove(user);
            newRoom.getUsers().add(user);
            roomRepository.save(oldRoom);
        }

        Room room = roomRepository.save(newRoom);
        return entityToDto(room);
    }

    public void removeUserFromRoom(Integer userId, Integer roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room cannot be found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User cannot be found"));
        room.getUsers().remove(user);
        roomRepository.save(room);
    }

    public RoomDTO addRoom(Room room) {
        return entityToDto(roomRepository.save(room));
    }

    private RoomDTO entityToDto(Room room) {
        return mapper.map(room, RoomDTO.class);
    }


    public RoomDTO getOrCreatePrivateRoom(int roomId, int userId, int sender) {
        User user1 = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user cannot be found"));
        User user2 = userRepository.findById(sender).orElseThrow(() -> new EntityNotFoundException("user cannot be found"));
        List<User> users = Arrays.asList(user1, user2);
        Room room = roomRepository.findByIdAndRoomType(roomId, RoomType.PUBLIC)
                .orElseThrow(() -> new UnsupportedOperationException("room cannot be found"));
        if (!validateUsersForChat(room.getUsers(), userId, sender)) {
            throw new UnsupportedOperationException("You are not able to chat outside room");
        }

        Integer privateRoomId = roomRepository.findByRoomTypeAndUsers(userId, sender);
        if (privateRoomId != null) {
            return entityToDto(roomRepository.findById(privateRoomId)
                    .orElseThrow(() -> new UnsupportedOperationException("Contact Administrator")));
        } else {
            Room newRoom = Room.builder().users(users).
                    name("private chat").date(new Date()).roomType(RoomType.PRIVATE).build();
            return entityToDto(roomRepository.save(newRoom));
        }
    }

    private boolean validateUsersForChat(List<User> users, int userId, int sender) {
        boolean firstUserExists = false;
        boolean secondUserExists = false;
        for (User user : users) {
            if (user.getId() == userId && !firstUserExists) {
                firstUserExists = true;
            }

            if (user.getId() == sender && !secondUserExists) {
                secondUserExists = true;
            }

            if (firstUserExists && secondUserExists) {
                return true;
            }
        }
        return false;
    }

    public RoomDTO validatePrivateRoomConnection(int roomId, Integer userId) {
        Room room = roomRepository.findByIdAndRoomType(roomId, RoomType.PRIVATE)
                .orElseThrow(() -> new UnsupportedOperationException("Unsupported connect, contact admin"));
        List<Integer> collect = room.getUsers().stream().map(User::getId).collect(Collectors.toList());
        if (collect.contains(userId)) {
            return entityToDto(room);
        } else {
            throw new UnsupportedOperationException("You are not allowed in this page");
        }
    }
}
