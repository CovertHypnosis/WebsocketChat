package com.example.websocketdemo.controller;

import com.example.websocketdemo.dtos.UserDTO;
import com.example.websocketdemo.services.RoomService;
import com.example.websocketdemo.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final RoomService roomService;

    public UserController(UserService userService, RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView addUser(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserDTO userDTO = userService.getOrCreateUser(username, password);
        request.getSession().setAttribute("id", userDTO.getId());
        return new ModelAndView("redirect:/room/");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        Integer roomId = (Integer) request.getSession().getAttribute("roomId");
        if (roomId != null && userId != null) {
            roomService.removeUserFromRoom(userId, roomId);
        }

        request.getSession().invalidate();
        return "index";
    }
}
