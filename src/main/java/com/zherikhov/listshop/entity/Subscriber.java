package com.zherikhov.listshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subscriber")
    private List<Contact> contacts;

    @Column(name = "step_status")
    private int stepStatus;

    @Column(name = "active_list")
    private int activeList;

    @CreationTimestamp
    @Column(name = "created", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created;

    public Subscriber() {
    }

    public Subscriber(long id, String userName, String firstName, String lastName) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contacts=" + contacts +
                ", stepStatus=" + stepStatus +
                ", created=" + created +
                '}';
    }
}
