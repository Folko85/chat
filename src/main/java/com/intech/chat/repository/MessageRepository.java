package com.intech.chat.repository;

import com.intech.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {


    @Query(nativeQuery = true, value = "SELECT * FROM message_tab m ORDER BY datetime LIMIT 20")
    List<Message> findLastMessage();
}
