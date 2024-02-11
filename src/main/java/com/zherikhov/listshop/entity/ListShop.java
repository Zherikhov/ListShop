package com.zherikhov.listshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "list_shops")
public class ListShop {

    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_subscriber")
    private Subscriber subscriber;

    @Column(name = "name")
    private String name;
}
