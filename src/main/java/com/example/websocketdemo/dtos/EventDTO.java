package com.example.websocketdemo.dtos;

import com.example.websocketdemo.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventDTO {
    private EventType type;
    private String text;
    private int senderId;
    private String senderName;
    private int roomId;
    private Date date;
}
