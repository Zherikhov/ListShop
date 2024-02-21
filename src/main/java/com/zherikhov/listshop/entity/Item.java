package com.zherikhov.listshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_list_shop")
    private ListShop listShop;

    @Column(name = "name")
    private String name;

    public Item() {
    }

    public Item(ListShop listShop, String name) {
        this.listShop = listShop;
        this.name = name;
    }
}
