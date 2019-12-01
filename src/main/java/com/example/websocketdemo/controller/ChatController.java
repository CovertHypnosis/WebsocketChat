package com.example.websocketdemo.controller;

import com.example.websocketdemo.dtos.EventDTO;
import com.example.websocketdemo.dtos.MessageDTO;
import com.example.websocketdemo.enums.EventType;
import com.example.websocketdemo.services.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

@Controller
@CrossOrigin
public class ChatController {
    private MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;

    public ChatController(MessageService messageService, SimpMessageSendingOperations messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/room/out/private/{id}")
    public void sendPrivateMessage(@Payload EventDTO eventDTO, @DestinationVariable("id") int id) {
        MessageDTO messageDTO = messageService.addMessage(eventDTO);
        EventDTO event = EventDTO.builder()
                .roomId(eventDTO.getRoomId())
                .senderId(messageDTO.getSender().getId())
                .senderName(messageDTO.getSender().getUsername())
                .date(new Date())
                .text(messageDTO.getText())
                .type(EventType.CHAT)
                .build();
        String url = "/topic/room/in/private/" + id;
        messagingTemplate.convertAndSend(url, event);
    }

    @MessageMapping("/room/out/{id}")
    public void sendMessage(@Payload EventDTO eventDTO, @DestinationVariable("id") int id) {
        MessageDTO messageDTO = messageService.addMessage(eventDTO);
        EventDTO event = EventDTO.builder()
                .roomId(eventDTO.getRoomId())
                .senderId(messageDTO.getSender().getId())
                .senderName(messageDTO.getSender().getUsername())
                .date(new Date())
                .text(messageDTO.getText())
                .type(EventType.CHAT)
                .build();
        String url = "/topic/room/in/" + id;
        messagingTemplate.convertAndSend(url, event);
    }
}
