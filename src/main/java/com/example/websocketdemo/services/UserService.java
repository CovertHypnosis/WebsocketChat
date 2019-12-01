package com.example.websocketdemo.services;

import com.example.websocketdemo.dtos.UserDTO;
import com.example.websocketdemo.model.User;
import com.example.websocketdemo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserService(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional
    public UserDTO getOrCreateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            return entityToDto(user.get());
        } else {
            User createdUser = createUserByUsernameAndPassword(username, password);
            return entityToDto(createdUser);
        }
    }

    private User createUserByUsernameAndPassword(String username, String password) {
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        return userRepository.save(user);
    }

    private UserDTO entityToDto(User user) {
        return mapper.map(user, UserDTO.class);
    }
}
