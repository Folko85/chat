package com.intech.chat.dto.response.socketio;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class SocketNotificationData {
    private int id;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @JsonProperty("sent_time")
    private Instant sentTime;
    @JsonProperty("entity_id")
    private Integer entityId;
    @JsonProperty("entity_author")
    private AuthorData entityAuthor;
    @JsonProperty("parent_entity_id")
    private int parentId;
    @JsonProperty("current_entity_id")
    private int currentEntityId;
}

