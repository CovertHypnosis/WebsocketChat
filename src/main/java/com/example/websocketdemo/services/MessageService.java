package com.example.websocketdemo.services;

import com.example.websocketdemo.dtos.EventDTO;
import com.example.websocketdemo.dtos.MessageDTO;
import com.example.websocketdemo.model.Message;
import com.example.websocketdemo.model.Room;
import com.example.websocketdemo.model.User;
import com.example.websocketdemo.repositories.MessageRepository;
import com.example.websocketdemo.repositories.RoomRepository;
import com.example.websocketdemo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper mapper;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository, RoomRepository roomRepository, ModelMapper mapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.mapper = mapper;
    }

    public MessageDTO addMessage(EventDTO eventDTO) {
        Room room = getRoomByType(eventDTO);
        User user = userRepository.findById(eventDTO.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("user with given id cannot be found"));
        if (room.getPublicMessages() == null) {
            room.setPublicMessages(new ArrayList<>());
        }
        Message message = Message.builder()
                .sender(user)
                .text(eventDTO.getText())
                .Date(new Date())
                .build();
        message = messageRepository.save(message);
        room.getPublicMessages().add(message);
        roomRepository.save(room);
        return entityToDto(message);
    }

    private Room getRoomByType(EventDTO eventDTO) {
        int roomId = eventDTO.getRoomId();
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("room with given id cannot be found"));
    }

    private MessageDTO entityToDto(Message message) {
        return mapper.map(message, MessageDTO.class);
    }
}
