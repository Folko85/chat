package com.intech.chat.repository;

import com.intech.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {


    @Query(nativeQuery = true, value = "SELECT * FROM public.message m  WHERE datetime >= (CURRENT_TIMESTAMP - INTERVAL '12 hours')")
    List<Message> findAllByDateTimeAfter(LocalDateTime dateTime);
}
