package pl.zhr.czappka.bazahr_poc.inbox;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Map;

class IncomingMessage {

    @Id
    Integer id;

    String type;

    Instant createdAt;

    Map<String, Object> payload;

    IncomingMessageStatus status;

    IncomingMessage(String type, Instant createdAt, Map<String, Object> payload, IncomingMessageStatus status) {
        this.type = type;
        this.createdAt = createdAt;
        this.payload = payload;
        this.status = status;
    }

    IncomingMessage(Integer id, String type, Instant createdAt, Map<String, Object> payload, IncomingMessageStatus status) {
        this.id = id;
        this.type = type;
        this.createdAt = createdAt;
        this.payload = payload;
        this.status = status;
    }

    @Override
    public String toString() {
        return "IncomingMessage{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                ", payload=" + payload +
                ", status=" + status +
                '}';
    }
}
