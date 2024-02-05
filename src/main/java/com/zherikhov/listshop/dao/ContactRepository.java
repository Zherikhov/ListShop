package com.zherikhov.listshop.dao;

import com.zherikhov.listshop.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
