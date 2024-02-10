package com.zherikhov.listshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_subscriber")
    private Subscriber subscriber;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "user_name")
    private String userName;

    public Contact() {
    }

    public Contact(Subscriber subscriber, String nickName, String userName) {
        this.subscriber = subscriber;
        this.nickName = nickName;
        this.userName = userName;
    }
}
