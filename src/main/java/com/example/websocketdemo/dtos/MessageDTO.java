package com.example.websocketdemo.dtos;

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
public class MessageDTO {
    private int id;
    private String text;
    private Date date = new Date();
    private UserDTO sender;
}
