package com.example.websocketdemo.controller;

import com.example.websocketdemo.dtos.RoomDTO;
import com.example.websocketdemo.model.Room;
import com.example.websocketdemo.services.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/room")
@CrossOrigin
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String getAllRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "dashboard";
    }

    @GetMapping("/{id}")
    public String renderPublicRoom(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        Integer oldRoomId = (Integer) request.getSession().getAttribute("roomId");
        RoomDTO room = roomService.synchronizeUserWithRooms(id, oldRoomId, userId);
        request.getSession().setAttribute("roomId", id);
        model.addAttribute("room", room);
        model.addAttribute("messages", room.getMessages());
        model.addAttribute("userId", userId);
        return "room";
    }

    @GetMapping("/private/room/{id}")
    public String renderPrivateRoom(@PathVariable("id")int id, Model model, HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        RoomDTO room = roomService.validatePrivateRoomConnection(id, userId);
        request.getSession().setAttribute("privateRoomId", room.getId());
        model.addAttribute("room", room);
        model.addAttribute("messages", room.getMessages());
        model.addAttribute("userId", userId);
        return "private";
    }

    @GetMapping("/private/{userId}")
    public ModelAndView getPrivateRoom(@PathVariable("userId") int userId, HttpServletRequest request) {
        Integer sender = (Integer) request.getSession().getAttribute("id");
        Integer roomId = (Integer) request.getSession().getAttribute("roomId");
        if (roomId == null || sender == null) {
            throw new UnsupportedOperationException("Cannot redirect, contact administrator");
        }
        RoomDTO privateRoom = roomService.getOrCreatePrivateRoom(roomId, userId, sender);
        return new ModelAndView("redirect:/room/private/room/" + privateRoom.getId());
    }

    @PostMapping("/")
    public void addRoom(String name) {
        Room room = Room.builder().name(name).build();
        roomService.addRoom(room);
    }
}
