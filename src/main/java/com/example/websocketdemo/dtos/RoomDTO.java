package com.example.websocketdemo.dtos;

import com.example.websocketdemo.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private int id;
    private String name;
    private RoomType roomType;
    private List<UserDTO> users;
    private List<MessageDTO> messages;
}
