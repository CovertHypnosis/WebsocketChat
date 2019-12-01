package com.example.websocketdemo;

import com.example.websocketdemo.enums.RoomType;
import com.example.websocketdemo.model.Room;
import com.example.websocketdemo.repositories.MessageRepository;
import com.example.websocketdemo.repositories.RoomRepository;
import com.example.websocketdemo.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
@Log4j2
public class WebsocketDemoApplication implements CommandLineRunner {
	private final RoomRepository roomRepository;

	public WebsocketDemoApplication(RoomRepository roomRepository, UserRepository userRepository, MessageRepository messageRepository) {
		this.roomRepository = roomRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebsocketDemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Room room = Room.builder().name("Java").date(new Date()).roomType(RoomType.PUBLIC).build();
		Room room2 = Room.builder().name("Web Sockets").date(new Date()).roomType(RoomType.PUBLIC).build();
		log.info(room);
		roomRepository.save(room);
		roomRepository.save(room2);
	}
}
