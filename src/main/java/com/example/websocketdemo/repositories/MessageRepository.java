package com.example.websocketdemo.repositories;

import com.example.websocketdemo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
