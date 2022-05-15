package com.intech.chat.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionRepository {
    private final static ConcurrentHashMap<Long, UUID> template = new ConcurrentHashMap<>();

    public List<UUID> findAll() {
        return new ArrayList<>(template.values());
    }

    public Optional<UUID> findByUserId(Long userId) {
        return Optional.ofNullable(template.get(userId));
    }

    public void save(Long userId, UUID sessionId) {
        template.put(userId, sessionId);
    }

    public void deleteByUserId(Long userId) {
        template.remove(userId);
    }

    public Optional<Long> findByUserUUID(UUID userUUID) {
        return template.keySet(userUUID).stream().findFirst();
    }

}
