package com.example.websocketdemo.model;

import com.example.websocketdemo.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users = new ArrayList<>();
    private Date date;
    private RoomType roomType = RoomType.PUBLIC;
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Message> publicMessages = new ArrayList<>();
}
