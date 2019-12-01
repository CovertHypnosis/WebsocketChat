package com.example.websocketdemo.controller;

import com.example.websocketdemo.services.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
public class WelcomeController {
    private final RoomService roomService;

    public WelcomeController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String welcome(HttpServletRequest request, Model model) {
        Integer id = (Integer) request.getSession().getAttribute("id");
        if (id != null) {
            model.addAttribute("rooms", roomService.getAllRooms());
            return "dashboard";
        } else {
            return "index";
        }
    }
}
