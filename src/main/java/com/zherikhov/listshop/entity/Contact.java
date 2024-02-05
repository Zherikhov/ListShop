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
    @JoinColumn(name = "id_contact")
    private Subscriber subscriber;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "user_name")
    private String userName;
}
